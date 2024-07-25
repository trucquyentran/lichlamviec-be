import React, { useEffect, useState } from 'react';
import { BrowserRouter, Route, Routes, Navigate, Outlet } from "react-router-dom";
import Interceptor from 'app/common/Interceptor';
import { Provider } from "react-redux";
import { createStore } from "redux";
import rootReducer from 'app/reducers';
import reportWebVitals from './reportWebVitals';
import App from 'app/App';
import LoginPage from 'app/views/pages/loginpage/LoginPage';
import ReactDOM from 'react-dom/client';
import Test from 'app/components/Test';
import NotificationComponent from 'app/components/NotificationComponent';
const store = createStore(
  rootReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
);

Interceptor.initInterceptor();

const Root = () => (
  <BrowserRouter>
    <Provider store={store}>
      <Routes>
                <Route exact path="/dang-nhap" element={ <LoginPage />} /> 
             

        <Route exact path="/*" element={<App />} />
      </Routes>
    </Provider>
  </BrowserRouter>
);

ReactDOM.createRoot(document.getElementById('root')).render(<Root />);
reportWebVitals();
