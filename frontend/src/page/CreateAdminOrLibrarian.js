import { api, showResult } from "../service/api/Api";

  export function CreateAdminOrLibrarian() {
    return `
      <div class="page">
        <h1>Create Admin / Librarian</h1>
        <div class="card">
          <h2>Create Admin</h2>
          <form id="form-create-admin">
            <div class="form-group">
              <label>Name</label>
              <input type="text" name="name" />
            </div>
            <div class="form-group">
              <label>Phone</label>
              <input type="text" name="phone" />
            </div>
            <div class="form-group">
              <label>Person ID</label>
              <input type="text" name="personId" />
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="text" name="email" />
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" name="password" />
            </div>
            <button type="submit" class="btn btn-primary">Create Admin</button>
          </form>
          <div id="result-create-admin"></div>
        </div>

        <div class="card">
          <h2>Create Librarian</h2>
          <form id="form-create-librarian">
            <div class="form-group">
              <label>Name</label>
              <input type="text" name="name" />
            </div>
            <div class="form-group">
              <label>Phone</label>
              <input type="text" name="phone" />
            </div>
            <div class="form-group">
              <label>Person ID</label>
              <input type="text" name="personId" />
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="text" name="email" />
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" name="password" />
            </div>
            <button type="submit" class="btn btn-primary">Create Librarian</button>
          </form>
          <div id="result-create-librarian"></div>
        </div>
        
        <div class="card">
        <h2>Create Author — POST /api/v1/author</h2>
        <form id="form-create-author">
          <div class="form-group">
            <label>Name</label>
            <input type="text" name="name" />
          </div>
          <button type="submit" class="btn btn-primary">Create</button>
        </form>
        <div id="result-create-author"></div>
      </div>

    <div class="card">
      <h2>Create Book — POST /api/v1/books</h2>
      <form id="form-create-book">
        <div class="form-group">
          <label>Title</label>
          <input type="text" name="title"/>
        </div>
        <div class="form-group">
          <label>Author ID</label>
          <input type="number" name="authorId"/>
        </div>
        <div class="form-group">
          <label>ISBN</label>
          <input type="text" name="isbn"/>
        </div>
        <div class="form-group">
          <label>Published Year</label>
          <input type="number" name="publishedYear"/>
        </div>
        <button type="submit" class="btn btn-primary">Create</button>
      </form>
      <div id="result-create-book"></div>
    </div>

    <div class="card">
      <h2>Edit Book — PATCH /api/v1/books/edit/{id}</h2>
      <form id="form-edit-book">
        <div class="form-group">
          <label>Book ID</label>
          <input type="number" name="id"/>
        </div>
        <div class="form-group">
          <label>Title</label>
          <input type="text" name="title"/>
        </div>
        <div class="form-group">
          <label>Author ID</label>
          <input type="number" name="authorId"/>
        </div>
        <div class="form-group">
          <label>ISBN</label>
          <input type="text" name="isbn"/>
        </div>
        <div class="form-group">
          <label>Published Year</label>
          <input type="number" name="publishedYear"/>
        </div>
        <button type="submit" class="btn btn-primary">Edit</button>
      </form>
      <div id="result-edit-book"></div>
    </div>

    <div class="card">
      <h2>Get Active Loans — GET /api/v1/loans</h2>
      <form id="form-get-active-loans">
        <div class="form-group">
          <label>Page</label>
          <input type="number" name="page" value="0"/>
        </div>
        <div class="form-group">
          <label>Size</label>
          <input type="number" name="size" value="10"/>
        </div>
        <button type="submit" class="btn btn-primary">Get Active</button>
      </form>
      <div id="result-get-active-loans"></div>
    </div>

    <div class="card">
      <h2>Loan History — GET /api/v1/loans/history</h2>
      <form id="form-loan-history">
        <div class="form-group">
          <label>Page</label>
          <input type="number" name="page" value="0" />
        </div>
        <div class="form-group">
          <label>Size</label>
          <input type="number" name="size" value="10" />
        </div>
        <button type="submit" class="btn btn-primary">Get History</button>
      </form>
      <div id="result-loan-history"></div>
    </div>
  </div>

  `;
  }

export async function mountCreateAdminOrLibrarian() {
  document.getElementById("form-create-admin")?.addEventListener("submit", async e => {
    e.preventDefault();
    const d = Object.fromEntries(new FormData(e.target));
    const {ok, data} = await api("POST", "/api/v1/member/admin", d);
    showResult("result-create-admin", ok, data);
  });

  document.getElementById("form-create-librarian")?.addEventListener("submit", async e => {
    e.preventDefault();
    const d = Object.fromEntries(new FormData(e.target));
    const {ok, data} = await api("POST", "/api/v1/member/librarian", d);
    showResult("result-create-librarian", ok, data);
  });

  document.getElementById('form-create-author')?.addEventListener('submit', async e => {
    e.preventDefault()
    const {name} = Object.fromEntries(new FormData(e.target))
    const {ok, data} = await api('POST', '/api/v1/author', {name})
    showResult('result-create-author', ok, data)
    });

  document.getElementById('form-create-book')?.addEventListener('submit', async e => {
    e.preventDefault()
    const d = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('POST', '/api/v1/books', {
      title: d.title,
      authorId: Number(d.authorId),
      isbn: d.isbn,
      publishedYear: Number(d.publishedYear),
    })
    showResult('result-create-book', ok, data)
  });

  document.getElementById('form-edit-book')?.addEventListener('submit', async e => {
    e.preventDefault()
    const d = Object.fromEntries(new FormData(e.target))
    const body = {}
    if (d.title) body.title = d.title
    if (d.authorId) body.authorId = Number(d.authorId)
    if (d.isbn) body.isbn = d.isbn
    if (d.publishedYear) body.publishedYear = Number(d.publishedYear)
    const { ok, data } = await api('PATCH', `/api/v1/books/edit/${d.id}`, body)
    showResult('result-edit-book', ok, data)
  });

  document.getElementById('form-get-active-loans')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { page, size } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v1/loans?page=${page}&size=${size}`)
    showResult('result-get-active-loans', ok, data)
  });

  document.getElementById('form-loan-history')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { page, size } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v1/loans/history?page=${page}&size=${size}`)
    showResult('result-loan-history', ok, data)
  });
  }