

		import java.util.ArrayList;
		import com.google.gson.*;
		import java.text.SimpleDateFormat;
		import java.util.Date;
		
		citiesList = new ArrayList();
		populationList = new ArrayList();
		statesList = new ArrayList();
		exportList = new ArrayList();

		Gson gson = new Gson();
		JsonArray jsonarray = gson.fromJson(inputCities.toString(), JsonArray.class);

		for (JsonElement e : jsonarray) {
			JsonObject jsonobj = e.getAsJsonObject();
			city = jsonobj.getAsJsonPrimitive("city").getAsString();
			citiesList.add(city);
			population=jsonobj.getAsJsonPrimitive("population").getAsString();
			populationList.add(population);
			state=jsonobj.getAsJsonPrimitive("state").getAsString();
			statesList.add(state);
		}
		
		println(citiesList);
		
		
		//currentWeatherList = new ArrayList();
		for(int i = 0; i < citiesList.size(); i++){
			if(i >= 200) break;
			
			map = new HashMap();
			String city = citiesList.get(i);
			city = city.replace(" ", "%20");
			String gsonUrl = "http://api.openweathermap.org/data/2.5/weather?q=" +city + "&APPID=b6370392ff2c8d934c5973a9cca95969";
			println("i=" + i + " gsonUrl=" + gsonUrl);
			URL url = new URL(gsonUrl);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader bufferedReader = null;
			try{ 
				bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}catch(java.io.FileNotFoundException e){

				map.put("temperature", "null");
				map.put("pressure", "null");
				map.put("humidity", "null");
				map.put("speed", "null");
				map.put("latitude", "null");
				map.put("longitude", "null");
				map.put("dateTime", dateTime.toString());
				map.put("city", city);
				map.put("population", population);
				map.put("state", state);

				exportList.add(map);
				continue;
			}
			
			StringBuffer response = new StringBuffer();
			while ((inputLine = bufferedReader.readLine()) != null) {
	            response.append(inputLine);
	        }
	        bufferedReader.close();
	        gson = new Gson();
	        println("response="+response);
			JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);

			code = jsonObject.getAsJsonPrimitive("cod").getAsString();
			
			Date date = new Date();
			SimpleDateFormat simpleDataFormat = new SimpleDateFormat("E, d MMM yyyy hh:mm:ss z");
			dateTime = simpleDataFormat.format(date);
					
			if("200".equals(code)){
				mainObject = jsonObject.getAsJsonObject("main");
				windObject =  jsonObject.getAsJsonObject("wind");
				coordObject =  jsonObject.getAsJsonObject("coord");
				
				temp = mainObject.getAsJsonPrimitive("temp").getAsString();
				pressure = mainObject.getAsJsonPrimitive("pressure").getAsString();
				humidity = mainObject.getAsJsonPrimitive("humidity").getAsString();

				speed = windObject.getAsJsonPrimitive("speed").getAsString();
				
				lat = coordObject.getAsJsonPrimitive("lat").getAsString();
				lon = coordObject.getAsJsonPrimitive("lon").getAsString();

				map.put("temperature", temp.toString());
				map.put("pressure", pressure.toString());
				map.put("humidity", humidity.toString());
				map.put("speed", speed.toString());
				map.put("latitude", lat.toString());
				map.put("longitude", lon.toString())
				map.put("dateTime", dateTime.toString());
				map.put("city", city);
				map.put("population", populationList.get(i).toString());
				map.put("state", statesList.get(i).toString());

				exportList.add(map);
				Thread.sleep(500);
			}
		}
		
	println("exportList=" + exportList);

	