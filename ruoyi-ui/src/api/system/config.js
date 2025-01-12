import request from '@/utils/request'

// 查询配置列表
export function selectConfigList(params) {
  return request({
    url: '/system/config/selectConfigList',
    method: 'get',
    params,
  })
}

// 查询配置信息
export function selectConfig(configId) {
  return request({
    url: '/system/config/selectConfig/' + configId,
    method: 'get',
  })
}

// 查询配置值
export function selectConfigValue(configKey) {
  return request({
    url: '/system/config/selectConfigValue/' + configKey,
    method: 'get',
  })
}

// 新增配置
export function insertConfig(data) {
  return request({
    url: '/system/config/insertConfig',
    method: 'post',
    data,
  })
}

// 修改配置
export function updateConfig(data) {
  return request({
    url: '/system/config/updateConfig',
    method: 'put',
    data,
  })
}

// 删除配置
export function deleteConfig(configIds) {
  return request({
    url: '/system/config/deleteConfig/' + configIds,
    method: 'delete',
  })
}

// 刷新配置缓存
export function refreshConfigCache() {
  return request({
    url: '/system/config/refreshConfigCache',
    method: 'delete',
  })
}
