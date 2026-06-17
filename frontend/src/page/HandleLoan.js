import { api, showResult } from "../service/api/Api";

export function HandleLoan() {
  return `
    <div class="page">
      <h1>Loans</h1>

      <div class="card">
        <h2>Create Loan — POST /api/v1/loans</h2>
        <form id="form-create-loan">
          <div class="form-group">
            <label>Book ID</label>
            <input type="number" name="bookId" />
          </div>
          <button type="submit" class="btn btn-primary">Create</button>
        </form>
        <div id="result-create-loan"></div>
      </div>

      <div class="card">
        <h2>Return Book — PATCH /api/v1/loans/{id}</h2>
        <form id="form-return-book">
          <div class="form-group">
            <label>Loan ID</label>
            <input type="number" name="id" />
          </div>
          <button type="submit" class="btn btn-primary">Return</button>
        </form>
        <div id="result-return-book"></div>
      </div>
    </div>
  `;
}

export async function mountHandleLoan() {

  document.getElementById('form-create-loan')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { bookId } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('POST', '/api/v1/loans', { bookId: Number(bookId) })
    showResult('result-create-loan', ok, data)
  })

  document.getElementById('form-return-book')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { id } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('PATCH', `/api/v1/loans/${id}`)
    showResult('result-return-book', ok, data)
  })
}
