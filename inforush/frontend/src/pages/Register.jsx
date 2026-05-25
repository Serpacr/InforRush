import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
export default function Register() {
  const { register } = useAuth(); const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState(''); const [loading, setLoading] = useState(false)
  const handleSubmit = async (e) => {
    e.preventDefault(); setError(''); setLoading(true)
    try { await register(form.name, form.email, form.password); navigate('/dashboard') }
    catch (err) { setError(err.response?.data?.error || 'Erro ao cadastrar') }
    finally { setLoading(false) }
  }
  return (
    <div className="auth-page">
      <div className="auth-box">
        <h2>Criar conta</h2>
        <p>Junte-se ao InfoRush e comece a publicar sobre animes!</p>
        <form onSubmit={handleSubmit}>
          <div className="form-group"><label>Nome</label><input type="text" placeholder="Seu nome" value={form.name} onChange={e => setForm(f => ({...f, name: e.target.value}))} required /></div>
          <div className="form-group"><label>E-mail</label><input type="email" placeholder="seu@email.com" value={form.email} onChange={e => setForm(f => ({...f, email: e.target.value}))} required /></div>
          <div className="form-group"><label>Senha</label><input type="password" placeholder="Mínimo 6 caracteres" value={form.password} onChange={e => setForm(f => ({...f, password: e.target.value}))} required minLength={6} /></div>
          {error && <div className="error-msg">{error}</div>}
          <button type="submit" className="btn btn-primary" style={{width:'100%',justifyContent:'center',marginTop:'.5rem'}} disabled={loading}>{loading ? 'Cadastrando...' : 'Criar conta'}</button>
        </form>
        <div className="divider">ou</div>
        <p style={{textAlign:'center',fontSize:'.9rem',color:'var(--text-muted)'}}>Já tem conta? <Link to="/login" style={{color:'var(--primary)'}}>Entrar</Link></p>
      </div>
    </div>
  )
}
