import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../services/api'
export default function EditPost() {
  const { id } = useParams(); const navigate = useNavigate()
  const [tags, setTags] = useState([]); const [form, setForm] = useState(null)
  const [error, setError] = useState(''); const [loading, setLoading] = useState(false)
  useEffect(() => {
    Promise.all([api.get(`/posts/${id}`), api.get('/tags')]).then(([post, allTags]) => {
      const p = post.data; setTags(allTags.data)
      setForm({ title:p.title, content:p.content, excerpt:p.excerpt||'', coverImage:p.coverImage||'', published:p.published, tagIds:p.tags?.map(t => t.id)||[] })
    }).catch(() => navigate('/dashboard'))
  }, [id])
  const toggleTag = (tid) => setForm(f => ({ ...f, tagIds: f.tagIds.includes(tid) ? f.tagIds.filter(t => t !== tid) : [...f.tagIds, tid] }))
  const handleSubmit = async (e) => {
    e.preventDefault(); setError(''); setLoading(true)
    try { const { data } = await api.put(`/posts/${id}`, form); navigate(`/post/${data.slug}`) }
    catch (err) { setError(err.response?.data?.error || 'Erro ao atualizar post') }
    finally { setLoading(false) }
  }
  if (!form) return <div className="loading">Carregando...</div>
  return (
    <div className="post-editor">
      <h1>Editar Post</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group"><label>Título *</label><input type="text" value={form.title} onChange={e => setForm(f => ({...f, title:e.target.value}))} required /></div>
        <div className="form-group"><label>Resumo</label><input type="text" value={form.excerpt} onChange={e => setForm(f => ({...f, excerpt:e.target.value}))} /></div>
        <div className="form-group"><label>Conteúdo *</label><textarea value={form.content} onChange={e => setForm(f => ({...f, content:e.target.value}))} required /></div>
        <div className="form-group"><label>Imagem de capa (URL)</label><input type="url" value={form.coverImage} onChange={e => setForm(f => ({...f, coverImage:e.target.value}))} /></div>
        <div className="form-group"><label>Tags</label><div className="tags-checkboxes">{tags.map(t => (<label key={t.id}><input type="checkbox" checked={form.tagIds.includes(t.id)} onChange={() => toggleTag(t.id)} />{t.name}</label>))}</div></div>
        <div className="form-group"><label style={{flexDirection:'row',alignItems:'center',gap:'.5rem',cursor:'pointer'}}><input type="checkbox" checked={form.published} onChange={e => setForm(f => ({...f, published:e.target.checked}))} />Publicado</label></div>
        {error && <div className="error-msg">{error}</div>}
        <div style={{display:'flex',gap:'.75rem',marginTop:'.5rem'}}>
          <button type="submit" className="btn btn-primary" disabled={loading}>{loading ? 'Salvando...' : '✅ Salvar Alterações'}</button>
          <button type="button" className="btn btn-ghost" onClick={() => navigate('/dashboard')}>Cancelar</button>
        </div>
      </form>
    </div>
  )
}
