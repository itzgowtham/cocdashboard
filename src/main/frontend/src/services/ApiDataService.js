import axios from "axios";
import { endPoint, API_PARAMS } from "../constants/EndPointConstants";

const BASE_URL = `${endPoint.BASE_URL}`;
const headers = {
  "Access-Control-Allow-Origin": "*",
};

export const PMPMfetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.SUMMARY}`,
    data: payload,
    headers,
  });
};

export const careCategoryFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.CARE_CATEGORY}`,
    data: payload,
    headers,
  });
};

export const providerSpecialtyFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.PROVIDER_SPECIALTY}`,
    data: payload,
    headers,
  });
};

export const KPIMetrics = () => {
  return axios({
    method: "get",
    url: `${BASE_URL}${API_PARAMS.LANDING_PAGE}`,
  });
};

export const FilterOptions = (authRequest) => {
  return axios({
    method: "get",
    url: `${BASE_URL}${API_PARAMS.FILTER_OPTIONS}`,
    data: authRequest,
  });
};

export const serviceRegionFetch = (data) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.SERVICE_REGION}`,
    headers,
    data,
  });
};

export const careProviderFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.CARE_PROVIDER}`,
    data: payload,
    headers,
  });
};

export const pcpGroupFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.PCP_GROUP}`,
    data: payload,
    headers,
  });
};
