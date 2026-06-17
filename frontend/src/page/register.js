import { api } from "../service/api/Api";
  import { showResult } from "../service/api/Api";

  export function Register() {
    return `
      <div class="page">
        <h1>Register</h1>
        <div class="card">
          <form id="form-register">
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
            <div class="form-group">
              <label>Password</label>
              <input type="password" name="password" />
            </div>
            <button type="submit" class="btn btn-primary">Register</button>
          </form>
          <div id="result-register"></div>
        </div>
      </div>
    `;
  }

  export async function mountRegister() {
    document.getElementById("form-register")?.addEventListener("submit", async e => {
      e.preventDefault();
      const d = Object.fromEntries(new FormData(e.target));
      const { ok, data } = await api("POST", "/api/v1/member", d);
      showResult("result-register", ok, data);
    });
  }