// Navbar fixed on scroll
window.addEventListener('scroll', function () {
  let navbar = document.getElementById("navbar");
  navbar.classList.toggle('fixed', window.scrollY > 0); // Corrected 'this.window' to 'window'
});

// Menu button toggle
menuBtn.onclick = function () {
  document.getElementById("nav-items").classList.toggle('active');

  // Change icon on click
  if (document.getElementById("nav-items").classList.contains('active')) {
    menuBtn.classList.remove("bx-menu");
    menuBtn.classList.add("bx-x");
  } else {
    menuBtn.classList.remove("bx-x");
    menuBtn.classList.add("bx-menu");
  }
};

// Menu section tab interaction
let menuTabs = document.querySelector('.menu-tabs'); // Corrected 'Document' to 'document'
menuTabs.addEventListener('click', function (e) {
  if (e.target.classList.contains('menu-tab-item') && !e.target.classList.contains('active')) {
    menuTabs.querySelector('.active').classList.remove('active');
    e.target.classList.add("active");
  } else {
    return;
  }
});

// Menu Toggle
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
  navigation.classList.toggle("active");
  main.classList.toggle("active");
};
