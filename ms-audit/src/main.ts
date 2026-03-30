import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Eureka } from 'eureka-js-client';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  const port = 3000;
  await app.listen(port);

  const client = new Eureka({
    instance: {
      app: 'ms-audit',
      hostName: 'ms-audit',
      ipAddr: 'ms-audit',
      statusPageUrl: `http://ms-audit:${port}/health`,
      port: {
        '$': port,
        '@enabled': true,
      },
      vipAddress: 'ms-audit',
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: 'service-registry',
      port: 8761,
      servicePath: '/eureka/apps/',
    },
  });

  client.logger.level('warn');
  console.log('Esperando a que el Service Registry esté listo...');
  setTimeout(() => {
    client.start((error) => {
      if (error) {
        console.log('Error al registrar en Eureka, reintentando en breve...');
      } else {
        console.log('¡Registro en Eureka exitoso!');
      }
    });
  }, 15000);

  console.log(`ms-audit corriendo en el puerto ${port}`);
}
bootstrap();