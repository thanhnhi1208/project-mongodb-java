$(document).ready(function(){
	
	$('#dataTable #deleteButton').on('click', function(event) {
		event.preventDefault();
		var href = $(this).attr('href');

		/* gắn giá trị vào nút confirm */
		$('#confirmDelete').attr('href', href);
		$('#deleteModal').modal();
	});
	
	$('#dataTable #editButton').on('click', function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		
		$.get(href, function(comment){
			$('#_idEdit').val(comment._id);
			$('#video_idEdit').val(comment.video_id);
			$('#comment_textEdit').val(comment.comment_text);
			$('#likesEdit').val(comment.likes);
			$('#repliesEdit').val(comment.replies);
		});
		
		$('#updateModal').modal();
	});
});