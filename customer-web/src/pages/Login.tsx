import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { webLogin } from "../api/auth";
import { useAuth } from "../AuthContext";
import Logo from "../components/Logo";

export default function Login() {
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      const result = await webLogin(name, phone);
      login(result.token);
      navigate("/");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Login failed");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="auth-screen">
      <form className="auth-card" onSubmit={handleSubmit}>
        <div style={{ alignSelf: "center" }}>
          <Logo size={56} showSubtitle />
        </div>
        <p className="auth-subtitle">Enter your name and phone number to order</p>
        <label>
          Name
          <input value={name} onChange={(e) => setName(e.target.value)} required />
        </label>
        <label>
          Phone number
          <input value={phone} onChange={(e) => setPhone(e.target.value)} required />
        </label>
        {error && <p className="auth-error">{error}</p>}
        <button type="submit" disabled={submitting}>
          {submitting ? "Signing in..." : "Continue"}
        </button>
      </form>
    </div>
  );
}
