# Use an official Node.js image as the base image
FROM node:16-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy  package.json and package-lock.json files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Build the React app
RUN npm run build

# Use a lightweight web server to serve the React app
FROM nginx:alpine

# Copy the build output to the nginx HTML directory
COPY --from=builder /app/build /usr/share/nginx/html

#Expose port 80
EXPOSE 80

# Start nginx when the container starts
CMD ["nginx", "-g", "daemon off;"]
