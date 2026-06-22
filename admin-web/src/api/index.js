import axios from 'axios'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000
})

http.interceptors.response.use(
  (res) => {
    if (res.data.code === 200) return res.data.data
    return Promise.reject(res.data)
  },
  (err) => Promise.reject(err)
)

export default {
  // 社区数据看板
  getDashboard(communityId) {
    return http.get(`/community/dashboard/${communityId}`)
  },

  // 老人列表
  getElderList(communityId, params) {
    return http.get(`/community/elders/${communityId}`, { params })
  },

  // 老人详情
  getElderDetail(userId) {
    return http.get(`/user/detail/${userId}`)
  },

  // 告警相关
  getAlertPage(params) {
    return http.get('/alert/page', { params })
  },
  getUnhandledAlerts(communityId) {
    return http.get(`/alert/unhandled/${communityId}`)
  },
  handleAlert(alertId, handlerId, result) {
    return http.post(`/alert/handle/${alertId}`, null, {
      params: { handlerId, result }
    })
  },
  getAlertsByUser(userId) {
    return http.get(`/alert/user/${userId}`)
  },

  // 上门帮扶
  addVisitRecord(record) {
    return http.post('/community/visit-record', record)
  },
  getVisitRecords(elderId, params) {
    return http.get(`/community/visit-records/elder/${elderId}`, { params })
  },

  // 健康报告
  getRecentReports(userId, days) {
    return http.get(`/health-report/recent/${userId}`, { params: { days } })
  },

  // 今日告警数
  getTodayAlertCount(communityId) {
    return http.get(`/alert/today-count/${communityId}`)
  }
}
