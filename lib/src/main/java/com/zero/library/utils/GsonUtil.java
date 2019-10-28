package com.zero.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class GsonUtil {

	/**
	 * 将json转换成对象
	 * @param jsonResult
	 * @param clz
	 * @return
	 * @throws JsonSyntaxException
	 */
	@SuppressWarnings("hiding")
	public static <T> T setJsonToBean(String jsonResult, Class<T> clz)
		throws JsonSyntaxException{
		try{
			Gson gson = new Gson();
			T t = gson.fromJson(jsonResult, clz);
			return t;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将对象转换成json数据
	 * @param clz
	 * @return
	 * @throws JsonSyntaxException
	 */
	public static String setBeanToJson(Object clz)
			throws JsonSyntaxException{
		try{
			Gson gson = new Gson();
			String json = gson.toJson(clz);
			return json;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将json转换成List
	 * @param json
	 * @param clz
	 * @return
	 * @throws JsonSyntaxException
	 */
	@SuppressWarnings("hiding")
	public static <T> List<T> setJsonToList(String json, Class<T> clz)
	throws JsonSyntaxException{
		try{
			Gson gson = new Gson();
			/*List<T> list = gson.fromJson(json, new TypeToken<List<T>>(){}.getType());*/
			List<T> list = new ArrayList<T>();
			JsonArray array = new JsonParser().parse(json).getAsJsonArray();
			for(final JsonElement elem : array){
				list.add(gson.fromJson(elem, clz));
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将List转换成json数据
	 * @param list
	 * @return
	 * @throws JsonSyntaxException
	 */
	@SuppressWarnings("hiding")
	public static <T> String setListToJson(List<T> list)
		throws JsonSyntaxException{
		try{
			Gson gson = new Gson();
			String json = gson.toJson(list);
			return json;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
