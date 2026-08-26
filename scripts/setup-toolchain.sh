#!/usr/bin/env bash
# One-time setup: registers a JDK 11 Maven toolchain in ~/.m2/toolchains.xml.
#
# The project's pom.xml requires a JDK 11 toolchain to build (see maven-toolchains-plugin),
# so that `mvn` always compiles with JDK 11 regardless of which JDK happens to launch Maven
# itself (e.g. a Homebrew-linked "latest" JDK). This script finds a JDK 11 installation on
# your machine and writes/updates ~/.m2/toolchains.xml to point at it.
set -euo pipefail

TOOLCHAINS_FILE="$HOME/.m2/toolchains.xml"

find_jdk11_macos() {
    /usr/libexec/java_home -v 11 2>/dev/null || true
}

find_jdk11_linux() {
    # 1. JAVA_HOME, if it's already a JDK 11
    if [ -n "${JAVA_HOME:-}" ] && [ -f "$JAVA_HOME/release" ] && grep -q '^JAVA_VERSION="11' "$JAVA_HOME/release"; then
        echo "$JAVA_HOME"
        return
    fi

    # 2. update-alternatives-registered JVMs
    if command -v update-alternatives >/dev/null 2>&1; then
        while read -r _ path _; do
            [ -z "$path" ] && continue
            local home
            home="$(dirname "$(dirname "$path")")"
            if [ -f "$home/release" ] && grep -q '^JAVA_VERSION="11' "$home/release"; then
                echo "$home"
                return
            fi
        done < <(update-alternatives --list java 2>/dev/null | xargs -I{} echo {} {} {})
    fi

    # 3. common install locations
    for dir in /usr/lib/jvm/*/ /opt/jdk*/ /opt/java/*/; do
        [ -d "$dir" ] || continue
        if [ -f "${dir}release" ] && grep -q '^JAVA_VERSION="11' "${dir}release"; then
            echo "${dir%/}"
            return
        fi
    done
}

OS="$(uname -s)"
case "$OS" in
    Darwin) JDK_HOME="$(find_jdk11_macos)" ;;
    Linux)  JDK_HOME="$(find_jdk11_linux)" ;;
    *)
        echo "Unsupported OS: $OS. On Windows, run scripts\\setup-toolchain.ps1 instead." >&2
        exit 1
        ;;
esac

if [ -z "${JDK_HOME:-}" ]; then
    echo "Could not find a JDK 11 installation on this machine." >&2
    echo "Install one, then re-run this script:" >&2
    if [ "$OS" = "Darwin" ]; then
        echo "  brew install --cask corretto11" >&2
    else
        echo "  sudo apt install openjdk-11-jdk   # Debian/Ubuntu" >&2
        echo "  sudo dnf install java-11-openjdk-devel   # Fedora/RHEL" >&2
    fi
    exit 1
fi

echo "Found JDK 11 at: $JDK_HOME"

mkdir -p "$(dirname "$TOOLCHAINS_FILE")"

if [ -f "$TOOLCHAINS_FILE" ] && grep -q '<version>11</version>' "$TOOLCHAINS_FILE"; then
    echo "$TOOLCHAINS_FILE already registers a JDK 11 toolchain — leaving it as-is."
    exit 0
fi

TOOLCHAIN_ENTRY="    <toolchain>
        <type>jdk</type>
        <provides>
            <version>11</version>
        </provides>
        <configuration>
            <jdkHome>${JDK_HOME}</jdkHome>
        </configuration>
    </toolchain>
</toolchains>"

if [ -f "$TOOLCHAINS_FILE" ]; then
    cp "$TOOLCHAINS_FILE" "$TOOLCHAINS_FILE.bak"
    echo "Existing $TOOLCHAINS_FILE backed up to $TOOLCHAINS_FILE.bak"
    # Drop the old closing </toolchains> line, then append the new <toolchain> block
    # (which ends with its own closing </toolchains> tag). Avoids awk -v, which chokes
    # on multi-line values in some awk implementations (e.g. macOS's).
    sed '/<\/toolchains>/d' "$TOOLCHAINS_FILE.bak" > "$TOOLCHAINS_FILE"
    printf '%s\n' "$TOOLCHAIN_ENTRY" >> "$TOOLCHAINS_FILE"
else
    cat > "$TOOLCHAINS_FILE" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<toolchains>
$TOOLCHAIN_ENTRY
EOF
fi

echo "Wrote JDK 11 toolchain to $TOOLCHAINS_FILE"
echo "Done. 'mvn compile' will now use JDK 11 for this project."
