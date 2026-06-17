import { api, showResult } from "../service/api/Api";

export function HandleAuthor() {
  return `
    <div class="page">
      <h1>Authors</h1>

      <div class="card">
        <h2>Get Author by ID — GET /api/v1/author/{id}</h2>
        <form id="form-get-author">
          <div class="form-group">
            <label>Author ID</label>
            <input type="number" name="id" />
          </div>
          <button type="submit" class="btn btn-primary">Get</button>
        </form>
        <div id="result-get-author"></div>
      </div>

      <div class="card">
        <h2>Get Books by Author — GET /api/v1/author/{authorId}/books</h2>
        <form id="form-get-author-books">
          <div class="form-group">
            <label>Author ID</label>
            <input type="number" name="authorId" />
          </div>
          <div class="form-group">
            <label>Page</label>
            <input type="number" name="page" value="0" />
          </div>
          <div class="form-group">
            <label>Size</label>
            <input type="number" name="size" value="10" />
          </div>
          <button type="submit" class="btn btn-primary">Get Books</button>
        </form>
        <div id="result-get-author-books"></div>
      </div>
    </div>
  `;
}

export async function mountHandleAuthor() {
  document.getElementById('form-create-author')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { name } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('POST', '/api/v1/author', { name })
    showResult('result-create-author', ok, data)
  })

  document.getElementById('form-get-author')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { id } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v1/author/${id}`)
    showResult('result-get-author', ok, data)
  })

  document.getElementById('form-get-author-books')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { authorId, page, size } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v1/author/${authorId}/books?page=${page}&size=${size}`)
    showResult('result-get-author-books', ok, data)
  })
}
