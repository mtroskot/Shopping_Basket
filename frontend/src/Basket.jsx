import React, { Component } from "react";

class Basket extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: {},
      basket: null,
      products: [],
      quantityToAdd: 0,
      productToAdd: "",
      freeProductDiscount: {
        amountOfProductsToBuy: 0,
        productToBuy: "",
        amountOfProductsToGetFree: 0,
        productToGetForFree: ""
      },
      productPriceDiscount: {
        amountOfProductsToBuy: 0,
        productToBuy: "",
        discountPercentage: 0,
        productToGetDiscount: ""
      }
    };
    this.ACCESS_TOKEN = "accessToken";
  }

  componentDidMount() {
    this.getCurrentUser();
    this.getProducts();
  }

  request = (url, options) => {
    const headers = new Headers({
      "content-type": "application/json"
    });
    if (localStorage.getItem(this.ACCESS_TOKEN)) {
      headers.append("authorization", "Bearer " + localStorage.getItem(this.ACCESS_TOKEN));
    }
    const defaults = { headers: headers };
    const fetchOptions = Object.assign({}, defaults, options);
    return fetch(url, fetchOptions);
  };

  getCurrentUser = () => {
    console.log(localStorage.getItem(this.ACCESS_TOKEN));
    if (!localStorage.getItem(this.ACCESS_TOKEN)) {
      return Promise.reject("No access token set.");
    }
    this.request(`http://localhost:8084/api/auth/currentUser`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(user => {
        console.log(user);
        this.setState({ user: user }, () => {
          this.getBasketByUser();
        });
      })
      .catch(error => {
        console.log(error);
      });
  };

  emptyBasket = () => {
    this.request(`http://localhost:8084/api/basket/empty`, {
      method: "POST",
      body: this.state.user.id
    })
      .then(response => response.json())
      .then(basket => {
        this.setState({ basket });
      });
  };

  getBasketByUser = () => {
    this.request(`http://localhost:8084/api/basket/get/${this.state.user.id}`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(basket => {
        this.setState({ basket });
        console.log(basket);
      });
  };

  getProducts = () => {
    this.request(`http://localhost:8084/api/product/all`, {
      method: "GET"
    })
      .then(response => response.json())
      .then(products => {
        let newFreeProductDiscount = { ...this.state.freeProductDiscount };
        newFreeProductDiscount.productToBuy = products[0].id;
        newFreeProductDiscount.productToGetForFree = products[0].id;
        let newProductPriceDiscount = { ...this.state.productPriceDiscount };
        newProductPriceDiscount.productToBuy = products[0].id;
        newProductPriceDiscount.productToGetDiscount = products[0].id;
        this.setState({
          products,
          productToAdd: products[0].id,
          freeProductDiscount: newFreeProductDiscount,
          productPriceDiscount: newProductPriceDiscount
        });
      })
      .catch(error => {
        console.log(error);
      });
  };

  addProductToBasket = async () => {
    const { user, quantityToAdd, productToAdd } = this.state;
    try {
      const response = await this.request(`http://localhost:8084/api/basket/add/${quantityToAdd}/${productToAdd}`, {
        method: "POST",
        body: user.id
      });
      if (response.status === 201) {
        alert("Product added");
        const basket = await response.json();
		console.log(basket);
        this.setState({ basket });
      } else {
        const serverError = await response.json();
        console.log(serverError);
        alert(serverError.message);
      }
    } catch (error) {
      console.log(error);
    }
  };

  addFreeProductDiscount = async () => {
    const { user, freeProductDiscount } = this.state;
    const { amountOfProductsToBuy, productToBuy, amountOfProductsToGetFree, productToGetForFree } = freeProductDiscount;
    try {
      const response = await this.request(
        `http://localhost:8084/api/basket/freeProductDiscount/${
          user.id
        }/${amountOfProductsToBuy}/${productToBuy}/${amountOfProductsToGetFree}/${productToGetForFree}`,
        {
          method: "POST"
        }
      );
      if (response.status === 201) {
        alert("Discount added");
        const basket = await response.json();
		console.log(basket);
        this.setState({ basket });
      } else {
        const serverError = await response.json();
        console.log(serverError);
        alert(serverError.message);
      }
    } catch (error) {
      console.log(error);
    }
  };

  addProductPriceDiscount = async () => {
    const { user, productPriceDiscount } = this.state;
    const { amountOfProductsToBuy, productToBuy, discountPercentage, productToGetDiscount } = productPriceDiscount;
    try {
      const response = await this.request(
        `http://localhost:8084/api/basket/productPriceDiscount/${user.id}/${amountOfProductsToBuy}/${productToBuy}/${discountPercentage /
          100}/${productToGetDiscount}`,
        {
          method: "POST"
        }
      );
      if (response.status === 201) {
        alert("Discount added");
        const basket = await response.json();
		console.log(basket);
        this.setState({ basket });
      } else {
        const serverError = await response.json();
        console.log(serverError);
        alert(serverError.message);
      }
    } catch (error) {
      console.log(error);
    }
  };

  handleInput = (event, menuName, isNumber = false) => {
    if (menuName) {
      let newMenuState = { ...this.state[menuName] };
      newMenuState[event.target.name] = isNumber ? Number(event.target.value) : event.target.value;
      this.setState({ [menuName]: newMenuState });
    } else {
      this.setState({ [event.target.name]: isNumber ? Number(event.target.value) : event.target.value });
    }
  };

  logout = () => {
    localStorage.removeItem(this.ACCESS_TOKEN);
    this.props.changeAuthState();
    this.setState({ errorMsg: "", isAuthenticated: false });
  };

  render() {
    const { user, products, quantityToAdd, productToAdd, freeProductDiscount, productPriceDiscount, basket } = this.state;
    const { amountOfProductsToGetFree, productToGetForFree } = freeProductDiscount;
    const { discountPercentage, productToGetDiscount } = productPriceDiscount;
    return (
      <div className="container">
        <div className="row">
          <div className="col">
            <div style={{ width: "60%", margin: "0 auto" }}>
              <span>{user.username}</span>
              <button onClick={this.logout}>Logout</button>
              <div>
                <input value={quantityToAdd} type="number" name="quantityToAdd" onChange={e => this.handleInput(e, null, true)} />
                <select value={productToAdd} name="productToAdd" className="form-control form-control-sm" onChange={this.handleInput}>
                  {products.map(product => {               
				   return (
                      <option value={product.id} key={product.id}>
                        {product.name+" "+product.pricePerUnit+"$"}
                      </option>
                    );
                  })}
                </select>
                <button onClick={this.addProductToBasket}>Add</button>
              </div>

              <div style={{ marginTop: "4em" }}>
                <h3>Buy</h3>
                <input
                  value={freeProductDiscount.amountOfProductsToBuy}
                  type="number"
                  name="amountOfProductsToBuy"
                  onChange={e => this.handleInput(e, "freeProductDiscount", true)}
                />
                <select
                  value={freeProductDiscount.productToBuy}
                  name="productToBuy"
                  className="form-control form-control-sm"
                  onChange={e => this.handleInput(e, "freeProductDiscount")}
                >
                  {products.map(product => {
                    return (
                      <option value={product.id} key={product.id}>
                        {product.name}
                      </option>
                    );
                  })}
                </select>
                <h3>get</h3>
                <input
                  value={amountOfProductsToGetFree}
                  type="number"
                  name="amountOfProductsToGetFree"
                  onChange={e => this.handleInput(e, "freeProductDiscount", true)}
                />
                <select
                  value={productToGetForFree}
                  name="productToGetForFree"
                  className="form-control form-control-sm"
                  onChange={e => this.handleInput(e, "freeProductDiscount")}
                >
                  {products.map(product => {
                    return (
                      <option value={product.id} key={product.id}>
                        {product.name}
                      </option>
                    );
                  })}
                </select>
                <h3>for free</h3>
                <button onClick={this.addFreeProductDiscount}>Add discount</button>
              </div>

              <div style={{ marginTop: "4em" }}>
                <h3>Buy</h3>
                <input
                  value={productPriceDiscount.amountOfProductsToBuy}
                  type="number"
                  name="amountOfProductsToBuy"
                  onChange={e => this.handleInput(e, "productPriceDiscount", true)}
                />
                <select
                  value={productPriceDiscount.productToBuy}
                  name="productToBuy"
                  className="form-control form-control-sm"
                  onChange={e => this.handleInput(e, "productPriceDiscount")}
                >
                  {products.map(product => {
                    return (
                      <option value={product.id} key={product.id}>
                        {product.name}
                      </option>
                    );
                  })}
                </select>
                <h3>get</h3>
                <input
                  value={discountPercentage}
                  type="number"
                  name="discountPercentage"
                  onChange={e => this.handleInput(e, "productPriceDiscount", true)}
                />
                <h3>% discount on</h3>
                <select
                  value={productToGetDiscount}
                  name="productToGetDiscount"
                  className="form-control form-control-sm"
                  onChange={e => this.handleInput(e, "productPriceDiscount")}
                >
                  {products.map(product => {
                    return (
                      <option value={product.id} key={product.id}>
                        {product.name}
                      </option>
                    );
                  })}
                </select>
                <button onClick={this.addProductPriceDiscount}>Add discount</button>
              </div>
            </div>
          </div>
          <div className="col">
            <h1>Basket</h1>
            {basket ? (
              <div>
                <p>Number of discounts {basket.discounts.length}</p>
                <p>TotalBillDiscountPercentage {basket.totalBillDiscountPercentage * 100}%</p>
                <p>TotalPriceWithDiscounts {Math.round(basket.totalPriceWithDiscounts * 100) / 100}$</p>
                <p>TotalPriceWithoutDiscounts {Math.round(basket.totalPriceWithoutDiscounts * 100) / 100}$</p>
                {Object.keys(basket.productsMap).map((key, i) => (
                  <p key={key}>
                    Product: {key.substring(2)} count: {basket.productsMap[key].length}
                  </p>
                ))}
                <p>Press F12 to see console for more details</p>
              </div>
            ) : (
              "Basket is currently empty"
            )}
            <div style={{ marginTop: "4em" }}>
              <button onClick={this.emptyBasket}>Empty basket</button>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Basket;
