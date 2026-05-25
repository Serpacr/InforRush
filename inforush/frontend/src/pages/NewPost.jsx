import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../services/api'

export default function NewPost() {
  const navigate = useNavigate()

  const [tags, setTags] = useState([])
  const [form, setForm] = useState({
    title: '',
    content: '',
    excerpt: '',
    coverImage: '',
    published: false,
    tagIds: []
  })

  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  // 🔥 FETCH TAGS (BLINDADO CONTRA QUALQUER FORMATO DE API)
  useEffect(() => {
    api.get('/tags')
      .then(r => {
        console.log('TAGS RESPONSE:', r.data)

        const data =
          r.data?.data ||
          r.data?.tags ||
          r.data?.content ||
          r.data

        setTags(Array.isArray(data) ? data : [])
      })
      .catch(err => {
        console.error('Erro ao buscar tags:', err)
        setTags([])
      })
  }, [])

  // 🔁 toggle de tags
  const toggleTag = (id) => {
    setForm(f => ({
      ...f,
      tagIds: f.tagIds.includes(id)
        ? f.tagIds.filter(t => t !== id)
        : [...f.tagIds, id]
    }))
  }

  // 📤 submit do post
  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const { data } = await api.post('/posts', form)
      navigate(`/post/${data.slug}`)
    } catch (err) {
      setError(err.response?.data?.error || 'Erro ao criar post')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="post-editor">
      <h1>Novo Post</h1>

      <form onSubmit={handleSubmit}>

        {/* TÍTULO */}
        <div className="form-group">
          <label>Título *</label>
          <input
            type="text"
            value={form.title}
            onChange={e =>
              setForm(f => ({ ...f, title: e.target.value }))
            }
            required
          />
        </div>

        {/* RESUMO */}
        <div className="form-group">
          <label>Resumo</label>
          <input
            type="text"
            value={form.excerpt}
            onChange={e =>
              setForm(f => ({ ...f, excerpt: e.target.value }))
            }
          />
        </div>

        {/* CONTEÚDO */}
        <div className="form-group">
          <label>Conteúdo *</label>
          <textarea
            value={form.content}
            onChange={e =>
              setForm(f => ({ ...f, content: e.target.value }))
            }
            required
          />
        </div>

        {/* IMAGEM */}
        <div className="form-group">
          <label>Imagem de capa</label>
          <input
            type="url"
            value={form.coverImage}
            onChange={e =>
              setForm(f => ({ ...f, coverImage: e.target.value }))
            }
          />
        </div>

        {/* TAGS (CORRIGIDO DEFINITIVAMENTE) */}
        <div className="form-group">
          <label>Tags</label>

          <div className="tags-checkboxes">
            {Array.isArray(tags) && tags.map(t => (
              <label key={t.id}>
                <input
                  type="checkbox"
                  checked={form.tagIds.includes(t.id)}
                  onChange={() => toggleTag(t.id)}
                />
                {t.name}
              </label>
            ))}
          </div>
        </div>

        {/* PUBLICAR */}
        <div className="form-group">
          <label>
            <input
              type="checkbox"
              checked={form.published}
              onChange={e =>
                setForm(f => ({ ...f, published: e.target.checked }))
              }
            />
            Publicar imediatamente
          </label>
        </div>

        {/* ERRO */}
        {error && <div className="error-msg">{error}</div>}

        {/* BOTÕES */}
        <div style={{ display: 'flex', gap: '1rem' }}>
          <button type="submit" disabled={loading}>
            {loading ? 'Salvando...' : 'Salvar Post'}
          </button>

          <button type="button" onClick={() => navigate('/dashboard')}>
            Cancelar
          </button>
        </div>

      </form>
    </div>
  )
}
