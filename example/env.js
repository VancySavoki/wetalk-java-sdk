const navigator = window.navigator;
const userAgent = navigator.userAgent;
const _authenticated = '_authenticated';
const getRedirectUrl = () => {
  const uri = () => {
    window.location.search.includes(_authenticated)
    ? window.location.href
    : (window.location.search.endsWith('&') ? window.location.href + _authenticated : window.location.href + '&' + _authenticated);
  };
  return encodeURIComponent(window.location.search === ''
  ? window.location.href + '?' + _authenticated
  : uri()
  );
};
// eslint-disable-next-line
const isWin32Platform = navigator.platform.toLowerCase().includes('win');
// eslint-disable-next-line
const iOS = {
  isMobile: userAgent.toLowerCase().indexOf('safari') >= 0
};
// eslint-disable-next-line
const Android = {
  isMobile: userAgent.toLowerCase().indexOf('android') >= 0
};
const Dingtalk = {
  dd: () => import('dingtalk-jsapi'),
  get corpId() { throw new Error('return your CorpId here') },
  get jssdk() {
    return new Promise((resolve, reject) => {
      Dingtalk.dd().then(dd => {
        dd.ready(function() {
          resolve(dd);
        });
      }).catch(reject);
    });
  },
  support: userAgent.toLowerCase().indexOf('dingtalk') >= 0
};
const Wechat = {
  wx: () => import('jweixin-1.2.0'), // 微信SDK需要自己导入到静态文件目录
  get corpId() { throw new Error('return your CorpId here') },
  get agentid() { throw new Error('return your agentid here') },
  scope: 'snsapi_base',
  get jssdk() {
    return new Promise((resolve, reject) => {
      Wechat.wx().then(wx => {
        wx.config({
          debug: process.env.NODE_ENV === 'development',
          appId: Wechat.corpId,
          timestamp: '',
          nonceStr: '',
          signature: '',
          jsApiList: []
        });
        wx.ready(() => resolve(wx));
        wx.error(reject);
      }).catch(reject);
    });
  },
  openWeixin: 'https://open.weixin.qq.com',
  support: userAgent.toLowerCase().indexOf('micromessenger') >= 0,
  get redirect() {
    return Wechat.openWeixin + `/connect/oauth2/authorize?response_type=code&agentid=${Wechat.agentid}&appid=${Wechat.corpId}&scope=${Wechat.scope}&redirect_uri=${encodeURIComponent(window.location.origin + '/')}&state=${getRedirectUrl()}#wechat_redirect`;
  }
};
export {
  userAgent, Dingtalk, Wechat, getRedirectUrl, _authenticated
};
