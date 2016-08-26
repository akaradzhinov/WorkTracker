/**
 * Created by mgaberov on 1/15/15.
 */
/**
 * Click handler for the left navigation - setting an 'active' state on click
 */
$('#mainnav-menu li').click(function () {
    $('#mainnav-menu li.active-link').removeClass('active-link');
    $(this).addClass('active-link');
});

/**
 initialize calendar widget
 */
document.addEventListener("DOMContentLoaded", function(event) {
    $('.calendarField').datetimepicker();
})

/**
 JS for image dialog on marketing page
 */
function makretingImageDialog() {
    $('#marketingImageModal').on('show.bs.modal', function (event) {

        var button = $(event.relatedTarget),// Button that triggered the modal
            url = button.data('url'); // Extract info from data-* attributes

            if (url) {
                var startDate   = button.data('startdate'),
                    expireDate  = button.data('expiredate'),
                    id          = button.data('id'),
                    targetUrl   = button.data('linkurl'),
                    position    = button.data('position'),
                    modal       = $(this);

                modal.find('.imgId').val(id);
                modal.find('.imgUrl').val(url);
                modal.find('.startDate').val(startDate);
                modal.find('.expireDate').val(expireDate);
                modal.find('.position').val(position);
                modal.find('.targetUrl').val(targetUrl);
            }
    })
}
