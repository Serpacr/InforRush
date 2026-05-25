import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import api from '../services/api'
export default function Dashboard() {
  const { user } = useAuth()
  const [posts, setPosts] = useState([]); const [loading, setLoading] = useState(true)
  useEffect(() => {
    api.get('/posts?size=50').then(r => {
      const all = r.data.content || []
      setPosts(all.filter(p => p.author?.id === user?.id))
    }).catch(() => setPosts([])).finally(() => setLoading(false))
  }, [])
  const handleDelete = async (id) => { if (!window.confirm('Excluir este post?')) return; await api.delete(`/posts/${id}`); setPosts(ps => ps.filter(p => p.id !== id)) }
  const fmt = (d) => new Date(d).toLocaleDateString('pt-BR')
  return (
    <div className="container dashboard">
      <div className="dashboard-header">
        <div><h1>Meus Posts</h1><p style={{color:'var(--text-muted)',fontSize:'.9rem',marginTop:'.25rem'}}>Olá, <strong>{user?.name}</strong>!</p></div>
        <Link to="/new-post" className="btn btn-primary">+ Novo Post</Link>
      </div>
      {loading ? <div className="loading">Carregando...</div> : posts.length === 0 ? (
        <div style={{textAlign:'center',padding:'4rem',color:'var(--text-muted)'}}>
          <div style={{fontSize:'3rem',marginBottom:'1rem'}}>📝</div>
          <p>Você ainda não publicou nada.</p>
          <Link to="/new-post" className="btn btn-primary" style={{marginTop:'1rem',display:'inline-flex'}}>Criar primeiro post</Link>
        </div>
      ) : (
        <div className="card" style={{overflow:'auto'}}>
          <table className="posts-table">
            <thead><tr><th>Título</th><th>Tags</th><th>Status</th><th>Data</th><th>Ações</th></tr></thead>
            <tbody>
              {posts.map(post => (
                <tr key={post.id}>
                  <td><Link to={`/post/${post.slug}`} style={{fontWeight:600}}>{post.title}</Link></td>
                  <td>{post.tags?.map(t => <span key={t.id} className="tag" style={{marginRight:'.3rem'}}>{t.name}</span>)}</td>
                  <td><span className={`badge ${post.published ? 'badge-green' : 'badge-gray'}`}>{post.published ? 'Publicado' : 'Rascunho'}</span></td>
                  <td>{fmt(post.createdAt)}</td>
                  <td><div style={{display:'flex',gap:'.4rem'}}><Link to={`/edit-post/${post.id}`} className="btn btn-ghost" style={{padding:'.3rem .6rem',fontSize:'.8rem'}}>✏️</Link><button onClick={() => handleDelete(post.id)} className="btn btn-danger" style={{padding:'.3rem .6rem',fontSize:'.8rem'}}>🗑️</button></div></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
