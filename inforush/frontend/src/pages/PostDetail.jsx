import { useState, useEffect } from 'react'
import { useParams, Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'
import api from '../services/api'
export default function PostDetail() {
  const { id } = useParams(); const { user } = useAuth(); const navigate = useNavigate()
  const [post, setPost] = useState(null); const [loading, setLoading] = useState(true)
  useEffect(() => { api.get(`/posts/${id}`).then(r => setPost(r.data)).catch(() => navigate('/')).finally(() => setLoading(false)) }, [id])
  const fmt = (d) => new Date(d).toLocaleDateString('pt-BR', { day: '2-digit', month: 'long', year: 'numeric' })
  const handleDelete = async () => { if (!window.confirm('Excluir este post?')) return; await api.delete(`/posts/${post.id}`); navigate('/') }
  if (loading) return <div className="loading">Carregando...</div>
  if (!post) return null
  const isOwner = user?.id === post.author?.id || user?.role === 'ADMIN'
  return (
    <div className="post-detail">
      <div style={{ display:'flex', gap:'.5rem', flexWrap:'wrap', marginBottom:'1rem' }}>{post.tags?.map(t => <span key={t.id} className="tag">{t.name}</span>)}</div>
      <h1>{post.title}</h1>
      <div className="post-meta"><span>✍️ {post.author?.name}</span><span>📅 {fmt(post.createdAt)}</span></div>
      {isOwner && (
        <div style={{ display:'flex', gap:'.5rem', marginBottom:'1.5rem' }}>
          <Link to={`/edit-post/${post.id}`} className="btn btn-ghost">✏️ Editar</Link>
          <button onClick={handleDelete} className="btn btn-danger">🗑️ Excluir</button>
        </div>
      )}
      {post.coverImage && <img src={post.coverImage} alt={post.title} className="post-cover" />}
      <div className="post-content">{post.content.split('\n').map((p, i) => p ? <p key={i}>{p}</p> : <br key={i} />)}</div>
      <div style={{ marginTop:'3rem', paddingTop:'1.5rem', borderTop:'1px solid var(--border)' }}><Link to="/" className="btn btn-ghost">← Voltar</Link></div>
    </div>
  )
}
