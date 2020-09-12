package com.efs.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class URL {

	public static List<Integer> decodeIntList(String valor) {
//		String[] vet = valor.split(",");
//		
//		List<Integer> list = new ArrayList<Integer>();
//		
//		for (int i = 0; i < vet.length; i++) {
//			list.add(Integer.parseInt(vet[i]));
//		}
//		return list;
//		ou
		  //                 transformando em lista               para cada elemento da lista     tranformando de novo em lista
		try {
			return Arrays.asList(valor.split(","))		.stream().map( id -> Integer.parseInt(id)).collect(Collectors.toList());
		} catch (java.lang.NumberFormatException e) {
			System.out.println("URL:: decodeIntList = " + valor);
			return null;
		}
	}
	
	/**
	 *  TV%de%LED = "TV de LED"
	 * @param valor
	 * @return
	 */
	public static String decodeParam(String valor) {
		try {
			return URLDecoder.decode(valor, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}