import { api } from "../service/api/Api";
  import { saveAuth } from "../service/storage/localStorage";

  export function Login() {
    return `
      <div class="page">
        <h1>Login</h1>
        <div class="card">
          <form id="form-login">
            <div class="form-group">
              <label>Email / Phone</label>
              <input type="text" name="username" />
            </div>
            <div class="form-group">
              <label>Password</label>
              <input type="password" name="password" />
            </div>
            <button type="submit">Logga in</button>
          </form>
          <div id="result-login"></div>
        </div>
      </div>
    `;
  }
  
  export async function mountLogin() {
    document.getElementById("form-login")?.addEventListener("submit", async e => {
      e.preventDefault();
      const { username, password } = Object.fromEntries(new FormData(e.target));
      const { ok, data } = await api("POST", "/api/v1/auth/login", { username, password });
      if (ok) {
        saveAuth(data.token, data.role);
        location.hash = "#/";
      } else {
        document.getElementById("result-login").innerHTML = `<div class="result result-error"><pre>${JSON.stringify(data, null, 
  2)}</pre></div>`;
      }
    });
  }