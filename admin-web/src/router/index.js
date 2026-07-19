import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/dashboard' },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/dashboard/index.vue')
  },
  {
    path: '/elders',
    name: 'Elders',
    component: () => import('../views/elders/index.vue')
  },
  {
    path: '/elder/:id',
    name: 'ElderDetail',
    component: () => import('../views/elder-detail/index.vue')
  },
  {
    path: '/alerts',
    name: 'Alerts',
    component: () => import('../views/alerts/index.vue')
  },
  {
    path: '/visits',
    name: 'Visits',
    component: () => import('../views/visit-records/index.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
