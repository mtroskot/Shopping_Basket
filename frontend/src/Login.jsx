import React, { Component } from "react";

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      errorMsg: "",
      usernameOrEmail: "",
      password: ""
    };
    this.ACCESS_TOKEN = "accessToken";
  }

  handleInput = (event, menuName, isNumber = false) => {
    if (menuName) {
      let newMenuState = { ...this.state[menuName] };
      newMenuState[event.target.name] = isNumber ? Number(event.target.value) : event.target.value;
      this.setState({ [menuName]: newMenuState });
    } else {
      this.setState({ [event.target.name]: isNumber ? Number(event.target.value) : event.target.value });
    }
  };

  request = (url, options) => {
    const headers = new Headers({
      "content-type": "application/json"
    });
    if (localStorage.getItem(this.ACCESS_TOKEN)) {
      headers.append("authorization", "Bearer " + localStorage.getItem(this.ACCESS_TOKEN));
    }
    const defaults = { headers: headers };
    const fetchOptions = Object.assign({}, defaults, options);
    return fetch(url, fetchOptions)
      .then(response => {
        if (!response.ok) {
          return Promise.reject(response);
        }
        return response.json();
      })
      .catch(err => {
        console.log(err);
        return Promise.reject(err);
      });
  };

  handleLoginSubmit = event => {
    event.preventDefault();
    const { usernameOrEmail, password } = this.state;
    const loginRequest = {
      usernameOrEmail,
      password
    };
    this.request(`http://localhost:8084/api/auth/login`, {
      method: "POST",
      body: JSON.stringify(loginRequest)
    })
      .then(response => {
        localStorage.setItem(this.ACCESS_TOKEN, response.accessToken);
        this.setState({ errorMsg: "", isAuthenticated: true });
        this.props.changeAuthState();
        console.log(response);
      })
      .catch(error => {
        const errorMsg = "Invalid username or password";
        this.setState({ errorMsg });
      });
  };

  render() {
    const { usernameOrEmail, password, errorMsg } = this.state;
    return (
      <div style={{ width: "60%", margin: "0 auto" }}>
        <div>
          <h2 style={{ color: "red" }}>{errorMsg}</h2>
          <form onSubmit={this.handleLoginSubmit}>
            <div className="form-group">
              <label htmlFor="exampleInputEmail1">Email address or username</label>
              <input
                value={usernameOrEmail}
                name="usernameOrEmail"
                className="form-control"
                id="exampleInputEmail1"
                placeholder="Enter email or username"
                onChange={this.handleInput}
              />
              <small id="emailHelp" className="form-text text-muted">
                We'll never share your email with anyone else.
              </small>
            </div>
            <div className="form-group">
              <label htmlFor="exampleInputPassword1">Password</label>
              <input
                value={password}
                name="password"
                type="password"
                className="form-control"
                id="exampleInputPassword1"
                placeholder="Password"
                onChange={this.handleInput}
              />
            </div>
            <button type="submit" className="btn btn-primary">
              Submit
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default Login;
