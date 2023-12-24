import { useState, useEffect, useRef } from "react";
import { Map, Placemark, YMaps } from "@pbe/react-yandex-maps";
import '../App.css';
import React from "react";
import courierService from "../services/courierService";

interface Coordinates {
    latitude:number;
    longitude:number;
}


const MainPage: React.FC = () => {
    
    const getLocation = () => {
        return new Promise<GeolocationPosition>((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(resolve, reject,{timeout:10000});
        });
    }

    const [currentCoordinates, setCurrentCoordinates] = useState<Coordinates | null>(null);
    const [mapCenter, setMapCenter] = useState<[number, number]>([55.75, 37.57]);

    const updateCoordinates = async () => {
        try {
            const position = await getLocation();
            const { latitude, longitude } = position.coords;

            if (!currentCoordinates || (currentCoordinates.latitude !== latitude && currentCoordinates.longitude !== longitude)) {
                setCurrentCoordinates(position.coords);
                await courierService.updateCoordinates(latitude, longitude);

                console.log([latitude, longitude]);
                const brand_new_var = [latitude, longitude];
                setMapCenter([brand_new_var[0], brand_new_var[1]]);
                console.log([mapCenter[0], mapCenter[1]]);
            }

        } catch (error) {
            console.error('Failed to get current position:', error);
        }
    };

    useEffect(() => {

        const intervalId = setInterval(updateCoordinates, 10000);

        return () => {
            clearInterval(intervalId);
        };
    }, []);

    const [ymaps, setYmaps] = useState<any>(null);
  const routes = useRef<any>(null);

  const getRoute = (ref: any) => {
    if (ymaps && currentCoordinates) {
      const multiRoute = new ymaps.multiRouter.MultiRoute(
        {
          referencePoints: [[currentCoordinates.latitude, currentCoordinates.longitude], "Рыбинск"],
          params: {
            results: 2
          }
        },
        {
        //   boundsAutoApply: true,
          routeActiveStrokeWidth: 6,
          routeActiveStrokeColor: "#fa6600"
        }
      );

      routes.current = multiRoute;
      ref.geoObjects.add(multiRoute);
    }
  };

  const getRoutes = () => {
    console.log(routes.current.getWayPoints());
  };

    return (
        <YMaps query={{ apikey: '8ec18778-cb70-437f-87fc-7c17e8e0bb71'}}>
            <Map
                className="map"
                state={{center: mapCenter, zoom: 9}}
                modules={["multiRouter.MultiRoute"]}
                onLoad={(ymaps) => setYmaps(ymaps)}
                instanceRef={(ref) => ref && getRoute(ref)}
            >
                {currentCoordinates && (
                    <Placemark geometry={[currentCoordinates.latitude, currentCoordinates.longitude]} />
                )}
            </Map>
        </YMaps>
    );
};


export default MainPage;