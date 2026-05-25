import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
export default function Login() {
  const { login } = useAuth(); const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState(''); const [loading, setLoading] = useState(false)
  const handleSubmit = async (e) => {
    e.preventDefault(); setError(''); setLoading(true)
    try { await login(form.email, form.password); navigate('/dashboard') }
    catch (err) { setError(err.response?.data?.error || 'Erro ao fazer login') }
    finally { setLoading(false) }
  }
  return (
    <div className="auth-page">
      <div className="auth-box">
        <h2>Entrar no InfoRush</h2>
        <p>Acesse sua conta para publicar e gerenciar posts.</p>
        <form onSubmit={handleSubmit}>
          <div className="form-group"><label>E-mail</label><input type="email" placeholder="seu@email.com" value={form.email} onChange={e => setForm(f => ({...f, email: e.target.value}))} required /></div>
          <div className="form-group"><label>Senha</label><input type="password" placeholder="••••••••" value={form.password} onChange={e => setForm(f => ({...f, password: e.target.value}))} required /></div>
          {error && <div className="error-msg">{error}</div>}
          <button type="submit" className="btn btn-primary" style={{width:'100%',justifyContent:'center',marginTop:'.5rem'}} disabled={loading}>{loading ? 'Entrando...' : 'Entrar'}</button>
        </form>
        <div className="divider">ou</div>
        <p style={{textAlign:'center',fontSize:'.9rem',color:'var(--text-muted)'}}>Não tem conta? <Link to="/register" style={{color:'var(--primary)'}}>Cadastre-se</Link></p>
        <div style={{marginTop:'1.5rem',padding:'.75rem',background:'rgba(255,255,255,.03)',borderRadius:'8px',fontSize:'.8rem',color:'var(--text-muted)'}}>
          <strong style={{color:'var(--text)'}}>Conta demo:</strong><br />admin@inforush.com / admin123
        </div>
      </div>
    </div>
  )
}
