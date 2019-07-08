import request from '@/utils/request'

export function getCreditsRule() {
  return request({
    url: '/creditsRule/getCreditsRule',
    method: 'post'
  })
}

export function saveCreditsRule(data) {
  return request({
    url: '/creditsRule/saveCreditsRule',
    method: 'post',
    data
  })
}
