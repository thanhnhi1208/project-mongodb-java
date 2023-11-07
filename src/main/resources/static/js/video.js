
$(document).ready(function(){
	
	$('#dataTable #editButton').on("click", function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		
		$.get(href, function(video){
			$('#_idEdit').val(video._id);
			$('#video_idEdit').val(video.video_id);
			$('#titleEdit').val(video.title);
			$('#channel_nameEdit').val(video.channel_name);
			$('#genresEdit').val(video.genres);
			$('#tagsEdit').val(video.tags);
			$('#viewsEdit').val(video.views);
			$('#likesEdit').val(video.likes);
			$('#dislikesEdit').val(video.dislikes);
			$('#dateEdit').val(video.date);
		});
		
		$('#updateModal').modal();
	});
	
	$('#dataTable #deleteButton').on('click', function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		
		$('#confirmDelete').attr('href', href);
		$('#deleteModal').modal();
	});
});