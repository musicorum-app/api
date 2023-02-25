-- CreateTable
CREATE TABLE "applications" (
    "id" VARCHAR(16) NOT NULL,
    "name" TEXT NOT NULL,
    "key" VARCHAR(32) NOT NULL,
    "secret" VARCHAR(32) NOT NULL,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3),

    CONSTRAINT "applications_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "collages" (
    "id" VARCHAR(24) NOT NULL,
    "app_id" VARCHAR(16),
    "success" BOOLEAN NOT NULL,
    "total_duration" DOUBLE PRECISION NOT NULL,
    "render_duration" DOUBLE PRECISION,
    "resources_duration" DOUBLE PRECISION,
    "file" TEXT,
    "theme" VARCHAR(32),
    "error" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3),

    CONSTRAINT "collages_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "applications_key_key" ON "applications"("key");

-- CreateIndex
CREATE UNIQUE INDEX "applications_secret_key" ON "applications"("secret");

-- AddForeignKey
ALTER TABLE "collages" ADD CONSTRAINT "collages_app_id_fkey" FOREIGN KEY ("app_id") REFERENCES "applications"("id") ON DELETE SET NULL ON UPDATE CASCADE;
