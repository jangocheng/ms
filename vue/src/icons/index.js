/*import Vue from 'vue'
import SvgIcon from '@/components/SvgIcon'// svg组件

// register globally
Vue.component('svg-icon', SvgIcon)

function requireAll (param) {
  return param.keys().map(param);
}*/

    // requireContext => requireContext.keys().map(requireContext)
const req = require.context('./svg', false, /\.svg$/)
// requireAll(req)
// debugger
req.keys().map(req);
