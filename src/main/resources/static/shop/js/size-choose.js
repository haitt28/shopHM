// state lưu lựa chọn
const selectedVariant = {
  size: null,
  color: null
};

$(document).on('click', function (e) {
  const target = e.target;
  // ===== CHỌN SIZE =====
  const sizeItem = target.closest('.size .item');
  if (sizeItem) {
    e.stopPropagation();

    $('.size .item').removeClass('size-choose');
    $(sizeItem).addClass('size-choose');

    selectedVariant.size = sizeItem.dataset.size;

    renderSizeColor();
    return;
  }

  // ===== CHỌN COLOR =====
  const colorItem = target.closest('.color .item');
  if (colorItem) {
    e.stopPropagation();

    $('.color .item').removeClass('color-choose');
    $(colorItem).addClass('color-choose');

    selectedVariant.color = colorItem.dataset.color;

    renderSizeColor();
  }
});

// render kết quả lên nút
function renderSizeColor() {
  if (selectedVariant.size && selectedVariant.color) {
    $('.size-details').text(
        selectedVariant.size + '/' + selectedVariant.color
    );
    $('.not-found-size').hide();
    $('#btn-buy-now').show();
    $('#modal-size-Choose').modal('hide'); // đóng khi đủ 2
  }
}


$('.size-guide').click(function () {
  $('body').addClass('modal1-on')
});

$('.go-back-size-choose').click(function () {
  $('body').addClass('modal2-on')
});

$('#sizeChooseModal').on('hidden.bs.modal', function () {
  $('body').addClass('modal1-on');

  if (!$('.modal.fade').hasClass('show')) {
    $('body').removeAttr('class');
  }
});

$('#sizeGuideModal').on('hidden.bs.modal', function () {
  $('body').addClass('modal2-on');

  if (!$('.modal.fade').hasClass('show')) {
    $('body').removeAttr('class');
  }
});