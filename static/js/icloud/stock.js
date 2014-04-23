function getCityWeather(cityName) {
	$.post(basepath + '/cityWeather/' + cityName, function(data) {
		$('.weather').html(data);
	});
}