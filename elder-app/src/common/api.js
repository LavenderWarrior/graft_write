const BASE_URL = 'http://localhost:8080/api'

function request(url, method, data) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data.data)
        } else {
          uni.showToast({ title: res.data.message || '请求失败', icon: 'none' })
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络请求失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

export default {
  // 用户相关
  login(openid, userType) {
    return request(`/user/login?openid=${openid}&userType=${userType}`, 'POST')
  },
  register(user) {
    return request('/user/register', 'POST', user)
  },
  getUserDetail(userId) {
    return request(`/user/detail/${userId}`, 'GET')
  },
  updateUser(user) {
    return request('/user/update', 'PUT', user)
  },
  getBoundElders(guardianId) {
    return request(`/user/bound-elders/${guardianId}`, 'GET')
  },

  // 权限相关
  getPermission(userId) {
    return request(`/permission/${userId}`, 'GET')
  },
  updatePermission(permission) {
    return request('/permission/update', 'PUT', permission)
  },
  toggleMonitor(userId, enable) {
    return request(`/permission/toggle-monitor?userId=${userId}&enable=${enable}`, 'POST')
  },
  agreePrivacy(userId) {
    return request(`/permission/agree-privacy?userId=${userId}`, 'POST')
  },

  // 绑定相关
  generateBindCode(elderId) {
    return request(`/binding/generate-code?elderId=${elderId}`, 'POST')
  },
  bindByCode(code, guardianId, guardianType) {
    return request(`/binding/bind-by-code?code=${code}&guardianId=${guardianId}&guardianType=${guardianType}`, 'POST')
  },
  getElderBindings(elderId) {
    return request(`/binding/elder/${elderId}`, 'GET')
  },
  unbind(bindingId) {
    return request(`/binding/unbind/${bindingId}`, 'POST')
  },

  // 健康报告
  getTodayReport(userId) {
    return request(`/health-report/today/${userId}`, 'GET')
  },
  getDailyReports(userId, start, end) {
    return request(`/health-report/daily/${userId}?start=${start}&end=${end}`, 'GET')
  },
  getWeeklyReport(userId, weekStart) {
    return request(`/health-report/weekly/${userId}?weekStart=${weekStart}`, 'GET')
  },
  getMonthlyReport(userId, year, month) {
    return request(`/health-report/monthly/${userId}?year=${year}&month=${month}`, 'GET')
  },
  getRecentReports(userId, days) {
    return request(`/health-report/recent/${userId}?days=${days}`, 'GET')
  },

  // 风险告警
  getAlertsByUser(userId) {
    return request(`/alert/user/${userId}`, 'GET')
  },
  cancelAlert(alertId, userId) {
    return request(`/alert/cancel/${alertId}?userId=${userId}`, 'POST')
  },
  getRecentAlerts(userId, limit) {
    return request(`/alert/recent/${userId}?limit=${limit}`, 'GET')
  },

  // 监护
  startMonitor(userId) {
    return request(`/monitor/start/${userId}`, 'POST')
  },
  stopMonitor(userId) {
    return request(`/monitor/stop/${userId}`, 'POST')
  },
  getCurrentMonitor(userId) {
    return request(`/monitor/current/${userId}`, 'GET')
  },

  // 会员
  getMembership(userId) {
    return request(`/membership/current/${userId}`, 'GET')
  },
  subscribe(userId, planType) {
    return request(`/membership/subscribe?userId=${userId}&planType=${planType}`, 'POST')
  },
  isVip(userId) {
    return request(`/membership/is-vip/${userId}`, 'GET')
  },

  // 社区
  getCommunityDashboard(communityId) {
    return request(`/community/dashboard/${communityId}`, 'GET')
  },
  getCommunityElders(communityId, keyword, page, size) {
    let url = `/community/elders/${communityId}?page=${page}&size=${size}`
    if (keyword) url += `&keyword=${keyword}`
    return request(url, 'GET')
  },
  addVisitRecord(record) {
    return request('/community/visit-record', 'POST', record)
  },
  getAlertPage(params) {
    let url = `/alert/page?page=${params.page || 1}&size=${params.size || 10}`
    if (params.userId) url += `&userId=${params.userId}`
    if (params.riskLevel) url += `&riskLevel=${params.riskLevel}`
    if (params.handleStatus !== undefined) url += `&handleStatus=${params.handleStatus}`
    return request(url, 'GET')
  },
  handleAlert(alertId, handlerId, result) {
    return request(`/alert/handle/${alertId}?handlerId=${handlerId}&result=${encodeURIComponent(result)}`, 'POST')
  },
  getUnhandledAlerts(communityId) {
    return request(`/alert/unhandled/${communityId}`, 'GET')
  },
  getTodayAlertCount(communityId) {
    return request(`/alert/today-count/${communityId}`, 'GET')
  }
}
