import http from './http';
import { Dingtalk, Wechat, userAgent, getRedirectUrl } from './env';
class AuthenticationProvider {
  authenticate() {}
  support() {}
}
class DingTalkAuthenticationProvider extends AuthenticationProvider {
  constructor() {
    super();
    this.client = Dingtalk;
  }
  authenticate() {
    this.client.jssdk.then(dd => {
      dd.runtime.permission.requestAuthCode({
        corpId: this.client.corpId,
        onSuccess: function({ code }) {
          const redirect = '/?code=' + code + '&userAgent=' + userAgent + '&state=' + getRedirectUrl();
          api.redirect(redirect);
        }
      });
    });
  }
  support() {
    return this.client.support;
  }
}
class WechatAuthenticationProvider extends AuthenticationProvider {
  constructor() {
    super();
    this.client = Wechat;
  }
  authenticate() {
    api.redirect(this.client.redirect);
  }
  support() {
    return this.client.support;
  }
}
;
const providers = [new WechatAuthenticationProvider(), new DingTalkAuthenticationProvider()];
const api = {
  userinfo() {
    const promise = new Promise((resolve, reject) => {
      http
      .get('userinfo')
      .then(res => {
        resolve(res.data);
      })
      .catch(({ response }) => {
        if (response.status === 401 || response.status === 403) {
          for (const provider of providers) {
            if (provider.support()) {
              provider.authenticate();
              break;
            }
          }
        }
      });
    });
    if (process.env.NODE_ENV === 'development') {
      return promise.then(res => ({
        ...res,
        alias: res.name,
        avatar: res.large_avatar,
        name: res.uid,
        'expires_in': new Date().getTime() + 86400 * 1000,
        authorities: [{ authority: 'ROLE_ADMIN' }]
      }));
    } else {
      return promise.then(res => ({
        ...res,
        alias: res.details.name,
        avatar: res.details.avatar,
        name: res.uid
      }));
    }
  },
  logout() { /* keeping empty */ },
  redirect(url) {
    window.location.href = url;
  }
};
export default api;
