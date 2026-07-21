package com.example.data.model

object CuratedVideos {
    val list = listOf(
        CuratedVideo(
            title = "Lo-Fi Beats Study Session (YouTube)",
            description = "Relaxing, ambient lo-fi beats streaming live from YouTube via the custom Liquid Glass embedding system.",
            videoUrl = "https://www.youtube.com/watch?v=jfKfPfyJRdk",
            thumbnailUrl = "https://images.unsplash.com/photo-1518495973542-4542c06a5843?w=500&auto=format&fit=crop&q=80",
            category = "YouTube / Chill",
            duration = "24:00"
        ),
        CuratedVideo(
            title = "NASA Space Walk - Ambient Orbit (YouTube)",
            description = "Breathtaking orbital cinematic captures of space walks and the International Space Station streaming from YouTube.",
            videoUrl = "https://www.youtube.com/watch?v=OnoNITE-CLc",
            thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&auto=format&fit=crop&q=80",
            category = "YouTube / Sci-Fi",
            duration = "18:35"
        ),
        CuratedVideo(
            title = "Big Buck Bunny (Adaptive HLS)",
            description = "A large and lovable rabbit deals with some mischievous woodland creatures in Mux's adaptive streaming HLS demo.",
            videoUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
            thumbnailUrl = "https://images.unsplash.com/photo-1585647347483-22b66260dfff?w=500&auto=format&fit=crop&q=80",
            category = "Animation / HLS",
            duration = "10:30"
        ),
        CuratedVideo(
            title = "Sintel (Adaptive HLS)",
            description = "The classic open-movie project Sintel, rendered beautifully as an adaptive HLS stream with multi-bitrate tracks.",
            videoUrl = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
            thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop&q=80",
            category = "Sci-Fi / HLS",
            duration = "14:48"
        ),
        CuratedVideo(
            title = "Tears of Steel (Sci-Fi MP4)",
            description = "A visual-effects heavy project featuring a giant robot and futuristic elements on progressive MP4 stream.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=500&auto=format&fit=crop&q=80",
            category = "Sci-Fi / MP4",
            duration = "12:14"
        ),
        CuratedVideo(
            title = "Elephant's Dream (Progressive MP4)",
            description = "The world's first open-source 3D animated film, presenting a surrealistic environment and intricate steam-punk architecture.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&auto=format&fit=crop&q=80",
            category = "Fantasy / MP4",
            duration = "10:54"
        ),
        CuratedVideo(
            title = "Big Buck Bunny (Progressive MP4)",
            description = "Classic progressive download MP4 of Big Buck Bunny for testing raw direct HTTP streaming.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=500&auto=format&fit=crop&q=80",
            category = "Animation / MP4",
            duration = "9:56"
        )
    )
}
