export function Nav(isLoggedIn = false, role = null) {
  return `
    <nav id="navbar">
      <span class="nav-brand">Library API</span>
      <div class="nav-links" id="navLinks">
        <a href="#/authors" class="nav-link">Authors</a>
        <a href="#/" class="nav-link">Books</a>
        ${isLoggedIn ? `<a href="#/loans" class="nav-link">Loans</a>` : ''}
        ${isLoggedIn && (role === 'ADMIN' || role === 'LIBRARIAN') ? `<a href="#/admin" class="nav-link">Admin</a>` : ''}
      </div>
      <div class="nav-login-register" id="navLogin">
        ${isLoggedIn
          ? `<a href="#/logout" class="nav-link">Logout</a>`
          : `<a href="#/register" class="nav-link">Register</a>
             <a href="#/login" class="nav-link">Login</a>`
        }
      </div>
    </nav>
  `
}
