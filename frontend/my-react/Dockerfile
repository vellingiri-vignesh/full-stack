FROM node:19-alpine
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "REACT_APP_BASE_URL=http://localhost:8084" > .env
EXPOSE 3000
CMD ["yarn", "start"]