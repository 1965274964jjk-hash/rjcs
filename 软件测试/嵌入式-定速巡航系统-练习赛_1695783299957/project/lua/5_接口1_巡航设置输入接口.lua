-- 程序：Input_setting

-- lua程序入口函数
function entry()
  --前提条件：设定速度大于0

  --停止状态，发送一个错误的类型，应做丢包处理，保持停止状态。
  write_msg(channels.setting, protocols.P_setting,{cmdType=1, dataValue=0xFFFF})--停止
  etimer.delay(100)
  local val1 = ask("yesno", {title='提示', msg='请确认被测件是否保持停止状态', default=true})
  check(val1, "执行关闭指令后，状态为停止状态，正确", "执行关闭指令后，状态为开启状态，错误")
  write_msg(channels.setting, protocols.P_setting,{cmdType=3, dataValue=0xFFFF})--错误类型
	local val2 = ask("yesno", {title='提示', msg='已发送错误指令，请确认被测件是否处于停止状态', default=true})
	check(val2, "停止状态执行错误指令，保持停止状态，正确", "停止状态执行错误指令，变成开启状态，错误")

  --开启状态,减速
  write_msg(channels.setting, protocols.P_setting,{cmdType=1, dataValue=0})
  etimer.delay(100)
	local res = pack(protocols.P_setting, {cmdType=2, dataValue=0xFFFF})
  local data = res.value
	write_buff(channels.setting, data)
	local val3 = ask("yesno",{title='提示', msg='请确认被测件设定速度是否减一', default=true})
  check(val3, "减速指令正常", "减速执行错误")
  --改为错误数据，重新发送
	for i=1,#test_data.test_input do
	  local change_index = test_data.test_input[i].change_index
		local change_title = test_data.test_input[i].change_title
		local tmpdata = ebuff.from_buff(data)--拷贝缓存数据
    tmpdata[change_index] = 0xCC
	  write_buff(channels.setting, tmpdata)
		local res = ask("yesno",{title='提示', msg='请确认被测件设定速度是否减一', default=true})
		check(not res, change_title.."错误有丢包处理，正确", change_title.."错误没有丢包处理，错误")
	end

	exit()
end
