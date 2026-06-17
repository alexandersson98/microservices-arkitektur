import { getToken } from "../storage/localStorage"


export function showResult(id, ok, data) {
  const el = document.getElementById(id)
  if (!el) return
  el.className = `result result-${ok ? 'success' : 'error'}`
  el.innerHTML = `<div class="result-header">${ok ? 'Success' : 'Error'}</div><pre>${JSON.stringify(data, null, 2)}</pre>`
}


export async function api(method, url, body) {
  try {
    const token = getToken()
    const headers = {}
    if (body) headers['Content-Type'] = 'application/json'
    if (token) headers['Authorization'] = `Bearer ${token}`
    const res = await fetch(url, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
    })
    const data = await res.json().catch(() => ({}))
    return { ok: res.ok, data }
  } catch (e) {
    return { ok: false, data: { message: e.message } }
  }
}
