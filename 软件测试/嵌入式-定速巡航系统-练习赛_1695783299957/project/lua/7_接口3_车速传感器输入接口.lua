-- 程序：Input_senser

-- lua程序入口函数
function entry()
	-- 此处输入程序代码
	local res = pack(protocols.P_speed, {speed=40})
    local data = res.value

	for i=1,#test_data.test_input do
	  local change_index = test_data.test_input[i].change_index
		local change_title = test_data.test_input[i].change_title

    --从原始数据创建新的数据缓存
		local tmpdata = ebuff.from_buff(data)

    tmpdata[change_index] = 0
	  write_buff(channels.Speed, tmpdata)
		local res = ask("yesno",{title='提示', msg='请确认被测件采集速度是否变为40', default=true})
		check(not res, change_title.."错误有丢包处理，正确", change_title.."错误没有丢包处理，错误")
		if(res)then
		    write_buff(channels.Speed, pack(protocols.P_speed, {speed=50}).value)
		end
    --print(data)
	end
	exit()
end
