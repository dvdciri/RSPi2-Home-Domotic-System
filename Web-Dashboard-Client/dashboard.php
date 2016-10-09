<html>
<head>
	<!--<meta http-equiv="refresh" content="5; URL=http://192.168.3.2/dashboard.php">-->
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>

	<link rel="icon" href="/media/icon.png">
	<title>Home Domotic System</title>
	
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">

	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.1.3/Chart.js"></script>

	<script src="https://cdn.firebase.com/js/client/2.4.1/firebase.js"></script>
	<script src="firebaseDataHelper.js"></script>

<style>

	body{
		padding:30px;
	}

	.control_icon{
		margin: 5px;
		width: 50px;
		height: 50px;
	}

	.control_btn{
		width: 100%;
	}

	.label-default{
		background:white;
		color: black;
		border: 1px solid transparent;
		border-color: #ccc;
	}

	#table_control_row, #table_control_row tr td:nth-child(2){
		width: 100%;
	}

	#loading{
		vertical-align: middle;
		visibility: hidden;
	}

	.active{
		visibility: visible !important;
	}
	
</style>
</head>
<body>
	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" id="dashboard_title" href="#"><label>Home Domotic System</label></a>
	    </div>

	    <form class="navbar-form navbar-right">
	    	<i id="loading" class="fa fa-spinner fa-pulse fa-2x fa-fw margin-bottom"></i>
	        <button data-toggle="modal" data-target="#myModal" type="button" class="btn btn-info">Settings</button>
	    </form>
	  </div>
	</nav>
	<div id="request_result"></div>
	

        <div class="panel panel-info">
            <div class="panel-heading"><b>Controls</b></div>
            <div class="panel-body">
            	<div class="col-lg-4 col-md-4 col-sm-4">
			        <div class="panel panel-success">
			            <div class="panel-heading"><b>Lights</b></div>
			            <div class="panel-body">
			            	<div id="lights-form" class="form"></div>
			            </div>
			        </div>
			    </div>
			    <div class="col-lg-4 col-md-4 col-sm-4">
			        <div class="panel panel-success">
			            <div class="panel-heading"><b>Blinds</b></div>
			            <div class="panel-body">
			            	<div id="blinds-form" class="form"></div>
			            </div>
			        </div>
			    </div>
			    <div class="col-lg-4 col-md-4 col-sm-4">
			        <div class="panel panel-warning">
			            <div class="panel-heading"><b>Sensors</b></div>
			            <div id="sensor_body" class="panel-body"></div>

			            <div class="table-responsive">
			            	<table class="table table-striped">
			            		<thead>
			            			<tr>
			            				<th>Name</th>
			            				<th>Current</th>
			            				<th>Max</th>
			            				<th>Min</th>
			            			</tr>
			            		</thead>
			            		<tbody id="tbody-sensors">
			            		</tbody>
			            	</table>
			            </div>
			        </div>
			    </div>
            </div>
        </div>
    

    <div class="col-lg-3 col-md-3 col-sm-3">
		<div class="panel panel-primary">
			<div class="panel-heading"><b>Weather</b></div>
			 <div class="panel-body">
			 	<div id="environment_container">
			 		<!-- weather widget start --><div id="m-booked-weather-bl250-92272"> <a href="//www.booked.net/weather/london-18114" class="booked-wzs-250-175" style="background-color:#137AE9;"> <div class="booked-wzs-250-175-data wrz-18"> <div class="booked-wzs-250-175-right"> <div class="booked-wzs-day-deck"> <div class="booked-wzs-day-val"> <div class="booked-wzs-day-number"><span class="plus">+</span>10</div> <div class="booked-wzs-day-dergee"> <div class="booked-wzs-day-dergee-val">&deg;</div> <div class="booked-wzs-day-dergee-name">C</div> </div> </div> <div class="booked-wzs-day"> <div class="booked-wzs-day-d">H: <span class="plus">+</span>10&deg;</div> <div class="booked-wzs-day-n">L: <span class="plus">+</span>7&deg;</div> </div> </div> <div class="booked-wzs-250-175-city">London</div> <div class="booked-wzs-250-175-date">Sunday, 09 October</div> <div class="booked-wzs-left"> <span class="booked-wzs-bottom-l">See 7-Day Forecast</span> </div> </div> </div> <table cellpadding="0" cellspacing="0" class="booked-wzs-table-250"> <tr> <td>Mon</td> <td>Tue</td> <td>Wed</td> <td>Thu</td> <td>Fri</td> <td>Sat</td> </tr> <tr> <td class="week-day-ico"><div class="wrz-sml wrzs-18"></div></td> <td class="week-day-ico"><div class="wrz-sml wrzs-18"></div></td> <td class="week-day-ico"><div class="wrz-sml wrzs-03"></div></td> <td class="week-day-ico"><div class="wrz-sml wrzs-18"></div></td> <td class="week-day-ico"><div class="wrz-sml wrzs-18"></div></td> <td class="week-day-ico"><div class="wrz-sml wrzs-18"></div></td> </tr> <tr> <td class="week-day-val"><span class="plus">+</span>13&deg;</td> <td class="week-day-val"><span class="plus">+</span>15&deg;</td> <td class="week-day-val"><span class="plus">+</span>12&deg;</td> <td class="week-day-val"><span class="plus">+</span>13&deg;</td> <td class="week-day-val"><span class="plus">+</span>14&deg;</td> <td class="week-day-val"><span class="plus">+</span>15&deg;</td> </tr> <tr> <td class="week-day-val"><span class="plus">+</span>4&deg;</td> <td class="week-day-val"><span class="plus">+</span>5&deg;</td> <td class="week-day-val"><span class="plus">+</span>8&deg;</td> <td class="week-day-val"><span class="plus">+</span>9&deg;</td> <td class="week-day-val"><span class="plus">+</span>8&deg;</td> <td class="week-day-val"><span class="plus">+</span>7&deg;</td> </tr> </table> </a> </div><script type="text/javascript"> var css_file=document.createElement("link"); css_file.setAttribute("rel","stylesheet"); css_file.setAttribute("type","text/css"); css_file.setAttribute("href",'//s.bookcdn.com/css/w/booked-wzs-widget-275.css?v=0.0.1'); document.getElementsByTagName("head")[0].appendChild(css_file); function setWidgetData(data) { if(typeof(data) != 'undefined' && data.results.length > 0) { for(var i = 0; i < data.results.length; ++i) { var objMainBlock = document.getElementById('m-booked-weather-bl250-92272'); if(objMainBlock !== null) { var copyBlock = document.getElementById('m-bookew-weather-copy-'+data.results[i].widget_type); objMainBlock.innerHTML = data.results[i].html_code; if(copyBlock !== null) objMainBlock.appendChild(copyBlock); } } } else { alert('data=undefined||data.results is empty'); } } </script> <script type="text/javascript" charset="UTF-8" src="http://widgets.booked.net/weather/info?action=get_weather_info&ver=4&cityID=18114&type=3&scode=124&ltid=3458&domid=&cmetric=1&wlangID=1&color=137AE9&wwidth=250&header_color=ffffff&text_color=333333&link_color=08488D&border_form=1&footer_color=ffffff&footer_text_color=333333&transparent=0"></script><!-- weather widget end -->
				</div>
			</div>
		</div>
	</div>

	<div class="col-lg-9 col-md-9 col-sm-9">
		<div class="panel panel-primary">
			<div class="panel-heading"><b>Raspberry PI</b></div>
			 <div class="panel-body">

				
			 	<div class="col-lg-4 col-md-4 col-sm-4">
			 		<div class="third widget pie">
				        <div id="canvas_container_ram" class="canvas-container text-center"></div>
				    </div>
			 	</div>
			 	<div class="col-lg-4 col-md-4 col-sm-4">
			 		<div class="text-center" id="cpu_container"></div>
			 	</div>
			 	<div class="col-lg-4 col-md-4 col-sm-4">
			 		<div class="third widget pie">
				        <div id="canvas_container_disk" class="canvas-container text-center"></div>
				    </div>
			 	</div>
			</div>
		</div>
	</div>



	


	
		 	


	<!-- Modal -->
	<div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">

	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Settings</h4>
	      </div>
	      <div class="modal-body">
	        <ul id="setting-list" class="list-group">
			</ul>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>

	  </div>
	</div>


</body>
</html>
