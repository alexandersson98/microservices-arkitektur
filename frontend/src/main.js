import './style.css'
import { Nav } from './components/NavBar.js';
import { isLoggedIn, getRole } from './service/storage/localStorage.js';
import { createRouter } from './router.js'

const app = document.querySelector("#app");
app.innerHTML = `
<header id="site-header"></header>
  <main id="outlet"></main>
  `;

export function renderNav() {
  document.querySelector("#site-header").innerHTML = Nav(isLoggedIn(), getRole());
}

renderNav();
createRouter("#outlet");