const ICON_DOTS = [
  { left: 68, top: 56, size: 28, color: "#FBF9F3" },
  { left: 35, top: 17, size: 16, color: "#C8455E" },
  { left: 19, top: 31, size: 16, color: "#FBF9F3" },
  { left: 15, top: 52, size: 16, color: "#C89A5B" },
  { left: 26, top: 71, size: 16, color: "#9FB18E" },
];

function PaletteIcon({ size }: { size: number }) {
  const scale = size / 108;
  return (
    <div style={{ position: "relative", width: size, height: size, borderRadius: "50%", background: "#7E9370", flex: "none" }}>
      {ICON_DOTS.map((dot, i) => (
        <div
          key={i}
          style={{
            position: "absolute",
            left: dot.left * scale,
            top: dot.top * scale,
            width: dot.size * scale,
            height: dot.size * scale,
            borderRadius: "50%",
            background: dot.color,
          }}
        />
      ))}
      {/* mustard center inside the cream dot */}
      <div
        style={{
          position: "absolute",
          left: (19 + 4) * scale,
          top: (31 + 4) * scale,
          width: 8 * scale,
          height: 8 * scale,
          borderRadius: "50%",
          background: "#E5B22C",
        }}
      />
      {/* brown center inside the sage dot */}
      <div
        style={{
          position: "absolute",
          left: (26 + 4.5) * scale,
          top: (71 + 4.5) * scale,
          width: 7 * scale,
          height: 7 * scale,
          borderRadius: "50%",
          background: "#6B5637",
        }}
      />
    </div>
  );
}

export default function Logo({ size = 40, showSubtitle = false }: { size?: number; showSubtitle?: boolean }) {
  return (
    <div style={{ display: "flex", alignItems: "center", gap: size * 0.22 }}>
      <PaletteIcon size={size} />
      <div style={{ display: "flex", flexDirection: "column", gap: 3 }}>
        <div style={{ fontSize: size * 0.44, fontWeight: 700, letterSpacing: "0.01em", color: "#1D1D1B", lineHeight: 1 }}>
          palette
        </div>
        {showSubtitle && (
          <div
            style={{
              fontFamily: "'DM Mono', ui-monospace, Menlo, monospace",
              fontSize: size * 0.11,
              letterSpacing: "0.24em",
              color: "#9C978B",
            }}
          >
            KITCHEN &amp; JUICE BAR
          </div>
        )}
      </div>
    </div>
  );
}
