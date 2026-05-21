import React from "react";
import "../../styles//public/HomePage.css";

import img1 from "../../assets/hero1.jpg";
import img2 from "../../assets/hero2.jpg";
import img3 from "../../assets/hero3.jpg";
import img4 from "../../assets/hero4.jpg";

import { Swiper, SwiperSlide } from "swiper/react";
import { Pagination, Autoplay } from "swiper/modules";

import "swiper/css";
import "swiper/css/pagination";

import Navbar from "../../components/Navbar";
import Footer from "../../components/Footer";

function HomePage() {
  return (
    <div className="home">
      {/* NAVBAR */}
      <Navbar />

      {/* CONTENUTO PRINCIPALE */}
      <main className="home-content">
        {/* HERO */}
        <section className="home-hero">
          <div className="home-hero-content">
            <h1>La tua salute, sempre con te.</h1>
            <p>
              Gestisci appuntamenti, referti, terapie e comunicazioni con il tuo medico
              in un’unica piattaforma sicura.
            </p>
          </div>
        </section>

        {/* CAROSELLO */}
        <section className="home-carousel">
          <Swiper
            modules={[Pagination, Autoplay]}
            pagination={{ clickable: true }}
            autoplay={{ delay: 3000 }}
            loop={true}
          >
            <SwiperSlide>
              <img src={img1} alt="Medico con cuore rosso" className="carousel-image" />
            </SwiperSlide>
            <SwiperSlide>
              <img src={img2} alt="Medico con paziente" className="carousel-image" />
            </SwiperSlide>
            <SwiperSlide>
              <img src={img3} alt="Tablet con dashboard medica" className="carousel-image" />
            </SwiperSlide>
            <SwiperSlide>
              <img src={img4} alt="Misurazione pressione" className="carousel-image" />
            </SwiperSlide>
          </Swiper>
        </section>
      </main>

      {/* FOOTER */}
      <Footer />
    </div>
  );
}

export default HomePage;
