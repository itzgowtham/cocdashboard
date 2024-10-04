import axios from "axios";
import { endPoint, API_PARAMS } from "../constants/EndPointConstants";

const BASE_URL = `${endPoint.BASE_URL}`;
const CHAT_URL = `${endPoint.CHAT_URL}`;
const headers = {
  "Access-Control-Allow-Origin": "*",
};
const chatHeaders = {
  accept: "application/json",
  "Content-Type": "application/json",
  "Access-Control-Allow-Origin": "*",
  "Disable-Loader": true,
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

export const providerSpecialtyDetailsFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.PROVIDER_SPECIALTY_Details}`,
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

export const pcpGroupDetailsFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.PCP_GROUP_DETAILS}`,
    data: payload,
    headers,
  });
};

export const forecastFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.FORECAST}`,
    data: payload,
    headers,
  });
};

export const careCategoryDetailsFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.CARE_CATEGORY_DETAILS}`,
    data: payload,
    headers,
  });
};

export const serviceRegionDetailsFetch = (data) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.SERVICE_REGION_DETAILS}`,
    headers,
    data,
  });
};

export const careProviderDetailsFetch = (payload) => {
  return axios({
    method: "post",
    url: `${BASE_URL}${API_PARAMS.CARE_PROVIDER_DETAILS}`,
    data: payload,
    headers,
  });
};

export const chatBotDataFetch = (payload) => {
  return axios({
    method: "post",
    url: `${CHAT_URL}${API_PARAMS.CHAT_BOT}`,
    data: payload,
    chatHeaders,
  });
};
