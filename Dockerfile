FROM node:14 AS builder
WORKDIR /src
COPY package*.json ./
COPY . .
RUN npm i
RUN npm run build

FROM node:14-alpine
WORKDIR /app

COPY --from=builder /src/dist ./dist
COPY --from=builder /src/package*.json ./
COPY --from=builder /.prisma ./
RUN npm ci
RUN npm prune --production

EXPOSE 3000
CMD [ "npm", "run", "start:prod" ]