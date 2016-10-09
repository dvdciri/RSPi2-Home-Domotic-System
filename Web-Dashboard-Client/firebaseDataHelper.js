$(document).ready(function() {

		//Network requests
		function toggleLight(pName, pNumber, piAddress) {
			var request = $.ajax({
				url: "http://"+piAddress+"/api/ToggleLight/"+pName+"/"+pNumber,
				type: "POST",
				dataType: "text"
			});

			request.done(function(msg) {
				setLoading(false);
				//$("#request_result").html('<div class="alert alert-success alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Success: </strong>'+msg+'</div>');
			});

			request.fail(function(jqXHR, textStatus) {
				setLoading(false);
				$("#request_result").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Request failed: </strong>'+ textStatus  + " with status: "+jqXHR.status+'</div>');

			});
		}

		function toggleBlind(pName, pNumber, piAddress) {
			var request = $.ajax({
				url: "http://"+piAddress+"/api/ToggleBlind/"+pName+"/"+pNumber,
				type: "POST",
				dataType: "text"
			});

			request.done(function(msg) {
				setLoading(false);
				//$("#request_result").html('<div class="alert alert-success alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Success: </strong>'+msg+'</div>');
			});

			request.fail(function(jqXHR, textStatus) {
				setLoading(false);
				$("#request_result").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Request failed: </strong>'+ textStatus  + " with status: "+jqXHR.status+'</div>');

			});
		}





		function loadContent(pi_address){
			var myFirebaseRef = new Firebase("https://dashmotic.firebaseio.com/Controls");
			myFirebaseRef.on("value", function(snapshot) {

				var lights_form = $("#lights-form");
				var blinds_form = $("#blinds-form");
				var sensors_table = $("#tbody-sensors");

				var class_on = "btn btn-warning";
				var class_off = "btn btn-default"

				//clear forms
				lights_form.html("");
				blinds_form.html("");
				sensors_table.html("");

				snapshot.forEach(function(childSnapshot) {
				    
				    //Create labels (Light, Sensors)
				    var node = childSnapshot.key();

				    childSnapshot.forEach(function(nodeChildSnapshot) {

				    	var node_child_key = nodeChildSnapshot.key();
				    	var node_child_value = nodeChildSnapshot.val();

				    	switch(node){
				    		case "Blinds":
				    			var button_class = (node_child_value.value == 0)?class_on:class_off;
								var blind_icon = node_child_value.icon;

								var html_blind_row = "<table id='table_control_row'><tr>";

								if(blind_icon != "" && blind_icon != null){
									html_blind_row = html_blind_row.concat("<td><img src='"+blind_icon+"' class='control_icon'></td>");
								}

								html_blind_row = html_blind_row.concat("<td><button class=\""+button_class+" control_btn\" id="+node_child_key+" type='button'>"+node_child_key+"</button></td>");

								html_blind_row = html_blind_row.concat("</tr></table>");
						    	//Create buttons
						    	blinds_form.append(html_blind_row);

						    	//Add on click listeners
						    	$("#"+node_child_key).click(function() {
						    		setLoading(true);
							  		toggleBlind(node_child_key, node_child_value.pin_number, pi_address);
								});
				    			break;
				    		case "Lights":
								var button_class = (node_child_value.value == 0)?class_on:class_off;
								var light_icon = node_child_value.icon;

								var html_light_row = "<table id='table_control_row'><tr>";

								if(light_icon != "" && light_icon != null){
									html_light_row = html_light_row.concat("<td><img src='"+light_icon+"' class='control_icon'></td>");
								}

								html_light_row = html_light_row.concat("<td><button class=\""+button_class+" control_btn\" id="+node_child_key+" type='button'>"+node_child_key+"</button></td>");

								html_light_row = html_light_row.concat("</tr></table>");
						    	//Create buttons
						    	lights_form.append(html_light_row);

						    	//Add on click listeners
						    	$("#"+node_child_key).click(function() {
						    		setLoading(true);
							  		toggleLight(node_child_key, node_child_value.pin_number, pi_address);
								});

				    			break;
				    		case "Sensors":
				    			
				    			sensors_table.append("<tr><td>"+node_child_key+"</td><td>"+node_child_value.current_inside+"</td><td>"+node_child_value.max_inside+"</td><td>"+node_child_value.min_inside+"</td></tr>");

				    			break;
				    	}
				    });
				  });

	  		});
		}
		//Load content end


		function loadPIInfo(){
			var myFirebaseRef = new Firebase("https://dashmotic.firebaseio.com/PI");
			myFirebaseRef.on("value", function(snapshot) {

				//Stop loading because everything is loaded completely at this point
				setLoading(false);

				snapshot.forEach(function(childSnapshot) {

					var node = childSnapshot.key();

				   	var node_value = childSnapshot.val();

				    switch(node){
						case "RAM":

							var canvas_container_ram = $("#canvas_container_ram");
							canvas_container_ram.html("<canvas id='ram_chart'></canvas>");


							var data = {
							    labels: [
							        "Free",
							        "Used"
							    ],
							    datasets: [
							        {
							            data: [node_value.free, node_value.used],
							            backgroundColor: [
							                "#FF6384",
							                "#36A2EB"
							            ]
							        }]
							};
							var options = {
							    responsive: false,
							    maintainAspectRatio: false,
							    title:{
							    	display: true,
							    	text: "RAM USAGE"
							    }
							};
							//Se up chart
							var ctx = $("#ram_chart");
							var myDoughnutChart = new Chart(ctx, {
							    type: 'doughnut',
							    data: data,
							    options: options
							});

							console.log("Free: " +node_value.free);

						break;
						case "DISK":

							var canvas_container_ram = $("#canvas_container_disk");
							canvas_container_ram.html("<canvas id='disk_chart'></canvas>");


							var data = {
							    labels: [
							        "Free",
							        "Used"
							    ],
							    datasets: [
							        {
							            data: [node_value.free, (node_value.total-node_value.free)],
							            backgroundColor: [
							                "#FF6384",
							                "#FFCE56"
							            ]
							        }]
							};
							var options = {
							    responsive: false,
							    maintainAspectRatio: false,
							    title:{
							    	display: true,
							    	text: "DISK SPACE"
							    }
							};
							//Se up chart
							var ctx = $("#disk_chart");
							var myDoughnutChart = new Chart(ctx, {
							    type: 'doughnut',
							    data: data,
							    options: options
							});

						break;
						case "CPU":
							var temp =  node_value.temperature;
							var color;
							var suggestion = "";
							
							if(temp < 10 || temp > 75){
								//Red
								color = "alert alert-danger";
								suggestion = "Not Good! Danger";
							}  else if(temp > 10 && temp < 25 || temp > 55 && temp < 75){
								//orange
								color = "alert alert-warning";
								suggestion = "Not So Good";
							}else if(temp > 25 && temp < 55){
								//green
								color = "alert alert-success";
								suggestion = "Good";
							}
								   
							$("#cpu_container").html("<h5 class='"+color+"'>CPU Temperature: "+temp+" °C ("+suggestion+")</h5>");
							


						break;
					}
				});
			});
		}

		function setLoading(loading){
			var load = $("#loading");
			if(loading){
				load.addClass("active");
			}else{
				load.removeClass("active");
			}
		}


		setLoading(true);
		//Load settings first
		var mySettingsRef = new Firebase("https://dashmotic.firebaseio.com/Settings");
		mySettingsRef.on("value", function(snapshot) {

			var settings = $("#setting-list");
			settings.html("");

			snapshot.forEach(function(childSnapshot) {

				console.log("child of settings ["+childSnapshot.key()+"]");
				switch(childSnapshot.key()){
					case "pi_address":

						console.log("pi_address ["+childSnapshot.val()+"]")

						//Once i've got the ip we are ready to load the content
						loadContent(childSnapshot.val());

						loadPIInfo();
						break;
					case "info_update_time_interval":

						$("#sensor_body").html("All the informations will be updated automatically every "+childSnapshot.val()+" seconds");
					break;
					case "last_update_datetime":

						$("#dashboard_title").html("<label>Home Domotic System</label> last update: "+childSnapshot.val()+"");
					break;
				}

				//Add setting to the setting list
				settings.append('<li class="list-group-item">'+childSnapshot.key()+': '+childSnapshot.val()+'</li>');

			});
		});


	});