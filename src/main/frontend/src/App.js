import "./App.css";
import Navigation from "./routes/Navigation";
import React, { useState, useEffect } from "react";
import axios from "axios";
import Spinner from "./components/spinner/Spinner";
import { DataProvider } from "./context/DataContext";

function App() {
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    const requestInterceptor = axios.interceptors.request.use((config) => {
      setLoading(true);
      return config;
    });

    const responseInterceptor = axios.interceptors.response.use(
      (response) => {
        setLoading(false);
        return response;
      },
      (error) => {
        setLoading(false);
        return Promise.reject(error);
      }
    );

    return () => {
      axios.interceptors.request.eject(requestInterceptor);
      axios.interceptors.response.eject(responseInterceptor);
    };
  }, [axios.interceptors.request, axios.interceptors.response]);

  return (
    <DataProvider>
      <Navigation />
      {loading && <Spinner></Spinner>}
    </DataProvider>
  );
}

export default App;
