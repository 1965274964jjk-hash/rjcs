-- 程序：speed

-- lua程序入口函数
function entry()

	--开启
	write_msg(channels.setting, protocols.P_setting,{dataValue=0})

	for i = 1, #test_data.testdata do
	    local inval = test_data.testdata[i].inval
		  local expval = test_data.testdata[i].expval
	    local res = write_msg(channels.Speed, protocols.P_speed,{speed=inval})
		  local val = ask("yesno",{title = "提示", msg="请确认采集车速为"..expval, default=true})
	    check( val, "车速为"..inval.."时采集正确","车速为"..inval.."时采集错误")
	end
	exit()
end
