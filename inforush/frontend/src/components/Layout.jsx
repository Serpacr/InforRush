import { Outlet, Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
export default function Layout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  return (
    <>
      <nav className="navbar">
        <div className="navbar-inner">
          <Link to="/" className="navbar-logo">Info<span>Rush</span></Link>
          <div className="navbar-links">
            {user ? (
              <>
                <Link to="/dashboard" className="btn btn-ghost">Meus Posts</Link>
                <Link to="/new-post" className="btn btn-primary">+ Novo Post</Link>
                <button onClick={() => { logout(); navigate('/') }} className="btn btn-ghost">Sair</button>
              </>
            ) : (
              <>
                <Link to="/login" className="btn btn-ghost">Entrar</Link>
                <Link to="/register" className="btn btn-primary">Cadastrar</Link>
              </>
            )}
          </div>
        </div>
      </nav>
      <main><Outlet /></main>
    </>
  )
}
