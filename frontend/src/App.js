import React from "react";
import Login from "./Login";
import Basket from "./Basket";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isAuthenticated: false
    };
    this.ACCESS_TOKEN = "accessToken";
  }

  changeAuthState = () => {
    this.setState({ isAuthenticated: !this.state.isAuthenticated });
  };

  render() {
    const { isAuthenticated } = this.state;
    return (
      <div className="App" style={{ alignItems: "center", justifyContent: "center" }}>
        {isAuthenticated ? <Basket changeAuthState={this.changeAuthState} /> : <Login changeAuthState={this.changeAuthState} />}
      </div>
    );
  }
}

export default App;
