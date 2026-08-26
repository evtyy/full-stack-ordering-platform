# One-time setup: registers a JDK 11 Maven toolchain in %USERPROFILE%\.m2\toolchains.xml.
#
# The project's pom.xml requires a JDK 11 toolchain to build (see maven-toolchains-plugin),
# so that `mvn` always compiles with JDK 11 regardless of which JDK happens to launch Maven
# itself. This script finds a JDK 11 installation on your machine and writes/updates
# toolchains.xml to point at it.
#
# Usage: powershell -ExecutionPolicy Bypass -File scripts\setup-toolchain.ps1

$ErrorActionPreference = "Stop"

function Test-Jdk11 {
    param([string]$Home)
    $releaseFile = Join-Path $Home "release"
    if (-not (Test-Path $releaseFile)) { return $false }
    return (Select-String -Path $releaseFile -Pattern '^JAVA_VERSION="11' -Quiet)
}

function Find-Jdk11 {
    # 1. JAVA_HOME, if it's already a JDK 11
    if ($env:JAVA_HOME -and (Test-Jdk11 $env:JAVA_HOME)) {
        return $env:JAVA_HOME
    }

    # 2. Common vendor install roots
    $roots = @(
        "$env:ProgramFiles\Amazon Corretto",
        "$env:ProgramFiles\Eclipse Adoptium",
        "$env:ProgramFiles\Java",
        "$env:ProgramFiles\Zulu",
        "$env:ProgramFiles\Microsoft"
    )

    foreach ($root in $roots) {
        if (-not (Test-Path $root)) { continue }
        foreach ($dir in Get-ChildItem -Path $root -Directory -ErrorAction SilentlyContinue) {
            if ($dir.Name -match "11" -and (Test-Jdk11 $dir.FullName)) {
                return $dir.FullName
            }
        }
    }

    return $null
}

$jdkHome = Find-Jdk11

if (-not $jdkHome) {
    Write-Error @"
Could not find a JDK 11 installation on this machine.
Install one, then re-run this script, e.g.:
  winget install Amazon.Corretto.11.JDK
"@
    exit 1
}

Write-Host "Found JDK 11 at: $jdkHome"

$m2Dir = Join-Path $env:USERPROFILE ".m2"
$toolchainsFile = Join-Path $m2Dir "toolchains.xml"
New-Item -ItemType Directory -Force -Path $m2Dir | Out-Null

if ((Test-Path $toolchainsFile) -and (Select-String -Path $toolchainsFile -Pattern '<version>11</version>' -Quiet)) {
    Write-Host "$toolchainsFile already registers a JDK 11 toolchain -- leaving it as-is."
    exit 0
}

$toolchainEntry = @"
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>11</version>
        </provides>
        <configuration>
            <jdkHome>$jdkHome</jdkHome>
        </configuration>
    </toolchain>
</toolchains>
"@

if (Test-Path $toolchainsFile) {
    Copy-Item $toolchainsFile "$toolchainsFile.bak" -Force
    Write-Host "Existing $toolchainsFile backed up to $toolchainsFile.bak"
    $content = Get-Content $toolchainsFile.bak -Raw
    $content = $content -replace '</toolchains>', $toolchainEntry
    Set-Content -Path $toolchainsFile -Value $content
} else {
    @"
<?xml version="1.0" encoding="UTF-8"?>
<toolchains>
$toolchainEntry
"@ | Set-Content -Path $toolchainsFile
}

Write-Host "Wrote JDK 11 toolchain to $toolchainsFile"
Write-Host "Done. 'mvn compile' will now use JDK 11 for this project."
