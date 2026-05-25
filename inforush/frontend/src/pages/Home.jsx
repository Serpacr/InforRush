import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import api from '../services/api'

export default function Home() {
  const [posts, setPosts] = useState([])
  const [tags, setTags] = useState([])
  const [activeTag, setActiveTag] = useState(null)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/tags')
      .then(r => {
        console.log('Tags:', r.data)

        setTags(
          Array.isArray(r.data)
            ? r.data
            : r.data.content || []
        )
      })
      .catch(err => {
        console.error('Erro ao carregar tags:', err)
        setTags([])
      })
  }, [])

  useEffect(() => {
    setLoading(true)

    const params = new URLSearchParams({
      page,
      size: 9
    })

    if (activeTag) {
      params.set('tag', activeTag)
    }

    api.get(`/posts?${params}`)
      .then(r => {
        console.log('Posts:', r.data)

        setPosts(r.data.content || [])
        setTotalPages(r.data.totalPages || 1)
      })
      .catch(err => {
        console.error('Erro ao carregar posts:', err)
        setPosts([])
      })
      .finally(() => setLoading(false))

  }, [page, activeTag])

  const fmt = (d) =>
    new Date(d).toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    })

  return (
    <div className="container">

      <div className="hero">
        <h1>
          Seu portal de <span>Animes</span>
        </h1>

        <p>
          Notícias, análises e curiosidades sobre o universo dos animes.
          Publicado por fãs, para fãs.
        </p>
      </div>

      <div className="tags-filter">

        <button
          className={`tag-filter-btn ${!activeTag ? 'active' : ''}`}
          onClick={() => {
            setActiveTag(null)
            setPage(0)
          }}
        >
          Todos
        </button>

        {Array.isArray(tags) && tags.map(t => (
          <button
            key={t.id}
            className={`tag-filter-btn ${activeTag === t.slug ? 'active' : ''}`}
            onClick={() => {
              setActiveTag(t.slug)
              setPage(0)
            }}
          >
            {t.name}
          </button>
        ))}

      </div>

      {loading ? (
        <div className="loading">
          Carregando posts...
        </div>

      ) : posts.length === 0 ? (

        <div className="loading">
          Nenhum post encontrado.
        </div>

      ) : (

        <div className="posts-grid">

          {posts.map(post => (

            <Link to={`/post/${post.slug}`} key={post.id}>

              <div className="card">

                <div className="post-card-img">
                  {post.coverImage ? (
                    <img
                      src={post.coverImage}
                      alt={post.title}
                    />
                  ) : (
                    <div className="post-card-img-placeholder">
                      ⚡
                    </div>
                  )}
                </div>

                <div className="post-card-body">

                  <div className="post-card-tags">
                    {post.tags?.map(t => (
                      <span key={t.id} className="tag">
                        {t.name}
                      </span>
                    ))}
                  </div>

                  <div className="post-card-title">
                    {post.title}
                  </div>

                  {post.excerpt && (
                    <div className="post-card-excerpt">
                      {post.excerpt}
                    </div>
                  )}

                  <div className="post-card-meta">
                    {post.author?.name} · {fmt(post.createdAt)}
                  </div>

                </div>
              </div>
            </Link>
          ))}

        </div>
      )}

      {totalPages > 1 && (
        <div className="pagination">

          <button
            onClick={() => setPage(p => p - 1)}
            disabled={page === 0}
          >
            ← Anterior
          </button>

          {Array.from(
            { length: totalPages },
            (_, i) => (
              <button
                key={i}
                className={page === i ? 'active' : ''}
                onClick={() => setPage(i)}
              >
                {i + 1}
              </button>
            )
          )}

          <button
            onClick={() => setPage(p => p + 1)}
            disabled={page >= totalPages - 1}
          >
            Próximo →
          </button>

        </div>
      )}
    </div>
  )
}