/* eslint-disable */
import axios from 'axios';
import api from './api';
const utils = require('axios/lib/utils');
const $t = msg => msg;
const success = msg => { };
const failed = (msg, err) => { console.error(msg, err); };
const globalErrorHandler = err => {
  if (err.__CANCEL__) return;
  if (err.response) {
    const { message, error_description, error, exception, msg } = err.response.data;
    let r = message 
    || error_description 
    || error 
    || exception 
    || msg 
    || (typeof err.response.data === 'string' ? err.response.data : JSON.stringify(err.response.data));
    r = r.toLocaleLowerCase();
    switch (err.response.status) {
      case 401: {
        failed($t('common.notice.nologin'), err);
        api.login();
        break;
      }
      case 400: {
        failed(r || $t('common.notice.badrequest'), err);
        break;
      }
      case 403: {
        failed(r || $t('common.notice.noauth'), err);
        api.login();
        break;
      }
      case 504: {
        failed(r || $t('common.notice.neterror'), err);
        break;
      }
      default: {
        failed(r, err);
        break;
      }
    }
  } else {
    failed(err.message);
  }
  return Promise.reject(err);
};

function encode(val) {
  return encodeURIComponent(val)
    .replace(/%40/gi, '@')
    .replace(/%3A/gi, ':')
    .replace(/%24/g, '$')
    .replace(/%2C/gi, ',')
    .replace(/%20/g, '+')
    .replace(/%5B/gi, '[')
    .replace(/%5D/gi, ']');
}

const paramsSerializer = params => {
  var parts = [];
  var serializedParams;
  utils.forEach(params, function serialize(val, key) {
    if (val === null || typeof val === 'undefined') {
      return;
    }
    if (!utils.isArray(val)) {
      val = [val];
    }
    utils.forEach(val, function parseValue(v) {
      if (utils.isDate(v)) {
        v = v.toISOString();
      } else if (utils.isObject(v)) {
        v = JSON.stringify(v);
      }
      parts.push(encode(key) + '=' + encode(v));
    });
  });
  serializedParams = parts.join('&');
  return serializedParams;
};
axios.defaults.timeout = 30000;
axios.defaults.baseURL = '/v2';
axios.defaults.headers.common['Accept'] = 'application/json;charset=utf8';
axios.interceptors.request.use(
  config => {
    config.paramsSerializer = paramsSerializer;
    const source = axios.CancelToken.source();
    config.cancelToken = source.token;
    return config;
  }, globalErrorHandler
);
axios.interceptors.response.use(
  response => {
    const methods = ['delete', 'post', 'put'];
    if (response.status < 300 && methods.some(method => response.config.method === method)) {
      if (!response.config.silent) {
        success($t('common.notice.success'));
      }
    }
    if (response.status < 300 && response.config.method === 'get' && !response.data) {
      success($t('common.notice.nodata'));
    }
    return response;
  }, globalErrorHandler
);
export default axios;
